package ru.itis.resourcemanagement.services.impl;

import com.google.common.collect.Sets;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.TimeEntryDto;
import ru.itis.resourcemanagement.dto.projections.ProjectInfo;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.exceptions.NotFoundException;
import ru.itis.resourcemanagement.model.*;
import ru.itis.resourcemanagement.repositories.TaskRepository;
import ru.itis.resourcemanagement.repositories.TimeEntryRepository;
import ru.itis.resourcemanagement.services.TaskService;
import ru.itis.resourcemanagement.services.TaskTypeService;
import ru.itis.resourcemanagement.util.DateUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskTypeService taskTypeService;
    private final TeamService teamService;
    private final TimeEntryRepository timeEntryRepository;
    private final ProjectService projectService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           @Lazy TaskTypeService taskTypeService, TeamService teamService, TimeEntryRepository timeEntryRepository,
                           @Lazy ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.taskTypeService = taskTypeService;
        this.teamService = teamService;
        this.timeEntryRepository = timeEntryRepository;
        this.projectService = projectService;
    }

    @Override
    public void createTask(Task task) {
        TaskType taskType = taskTypeService.getTaskTypeById(task.getType().getId());
        double estimate = taskType.getManHourPerUnit() * task.getUnitValue();
        task.setEstimate(estimate);
        task.setType(taskType);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public TimeEntry track(long taskId, TimeEntryDto dto, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(NotFoundException::new);
        task.setFactTime(task.getFactTime() + dto.getTime());
        TimeEntry timeEntry = TimeEntry.builder()
                .time(dto.getTime())
                .date(dto.getDate())
                .comment(dto.getComment())
                .task(task)
                .employee(user)
                .build();
        return timeEntryRepository.save(timeEntry);
    }

    @Override
    public List<Task> findByTaskType(TaskType taskType) {
        return taskRepository.findAllByType(taskType);
    }

    @Override
    @Transactional
    public List<TaskListInfo> getTasksForUser(User user) {
        if (user.getPosition().equals(Position.EMPLOYEE)) {
            return taskRepository.getTaskByAssignee(user);
        } else if (user.getPosition().equals(Position.PROJECT_SUPERVISOR)) {
            List<Long> projectIdList = projectService.getProjects(user).stream()
                    .map(ProjectInfo::getId)
                    .collect(Collectors.toUnmodifiableList());
            return taskRepository.findAllByProjectIdIn(projectIdList);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public TaskListInfo getTask(long taskId, User user) {
        return taskRepository.findTaskByIdAndAssignee(taskId, user)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<TaskListInfo> getTasksForProject(Long id) {
        return taskRepository.getTaskByProjectId(id);
    }

    @Override
    @Transactional
    public void assignTaskToTeam(Long id, Long teamId, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        Team team = teamService.findTeam(teamId)
                .orElseThrow(NotFoundException::new);
        task.setTeam(team);
    }

    @Scheduled(cron = "0 0 2 * * MON-FRI")
    public void setAbnormalTasks() {
        taskRepository.setAbnormalTasks();
    }


    /*
    Алгоритм: 1) топологическая сортировка задач
    добавить в список зависимые задания до этого не распределенные

     */
    public void distributeTasks(TaskSet taskSet) {
        List<Task> tasks = new ArrayList<>(taskSet.getTasks());

        Graph<Task, DefaultEdge> taskGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        for (Task task : tasks) {
            taskGraph.addVertex(task);
            for (Task subtask : task.getSubtasks()) {
                if (subtask.getTaskStatus().equals(TaskStatus.OPEN)) {
                    taskGraph.addVertex(subtask);
                    taskGraph.addEdge(subtask, task);
                }
            }
        }
        TopologicalOrderIterator<Task, DefaultEdge> topologicalOrder = new TopologicalOrderIterator<>(taskGraph);
        topologicalOrder.forEachRemaining(this::distribute);
    }

    public void distribute(Task task) {
        List<Team> teams = teamService.getTeamsForType(task.getType());
        List<User> users = teams.stream().
                flatMap(team -> team.getMembers().stream())
                .collect(Collectors.toList());
        for (User user : users) {
            List<Task> usersOpenTasks = taskRepository.findByAssigneeAndTaskStatus(user, TaskStatus.OPEN);
            double timeRemain = usersOpenTasks.stream()
                    .mapToDouble(t -> Math.max(t.getEstimate() - t.getFactTime(), 0.0))
                    .sum();
            boolean hasEstimateViolation = usersOpenTasks.stream()
                    .anyMatch(t -> t.getFactTime() > t.getEstimate());
            Project project = usersOpenTasks.stream()
                    .collect(Collectors.groupingBy(Task::getProject, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            user.setEstimateIntruder(hasEstimateViolation);
            user.setTimeRemain(timeRemain);
            user.setCurrentWorkingProject(project);
        }

        int daysTillDeadline = DateUtils.workingDaysBetween(LocalDate.now(), task.getDeadline().minusDays(1L));

        users.stream()
                .filter(user -> !user.isEstimateIntruder()
                        && daysTillDeadline > task.getEstimateCalculated() / user.getGrade().getCoefficient() / 8
                        && task.getProject().equals(user.getCurrentWorkingProject()))
                .findAny()
                .or(() -> users.stream()
                        .filter(user -> !user.isEstimateIntruder()
                                && daysTillDeadline > task.getEstimateCalculated() / user.getGrade().getCoefficient() / 8
                        ).findAny()
                ).or(() -> users.stream()
                        .filter(user -> !user.isEstimateIntruder())
                        .findAny()
                ).or(() -> users.stream().findAny())
                .ifPresent(task::setAssignee);
    }
}
