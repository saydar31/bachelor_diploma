package ru.itis.resourcemanagement.services.impl;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.TaskDistributionDto;
import ru.itis.resourcemanagement.dto.TimeEntryDto;
import ru.itis.resourcemanagement.dto.projections.ProjectInfo;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.dto.projections.TimeEntryInfo;
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
        Project project = projectService.getProjectById(task.getProject().getId());
        task.setType(taskType);
        task.setProject(project);
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
    @Transactional
    public List<TaskListInfo> getTasksForUser(User user) {
        return getTaskListByUserAndStatus(user, TaskStatus.OPEN);
    }

    @Override
    @Transactional
    public List<TaskListInfo> getClosedTasksForUser(User user) {
        return getTaskListByUserAndStatus(user, TaskStatus.CLOSED);
    }

    @Override
    @Transactional
    public Collection<Task> distribute(TaskDistributionDto taskDistribution) {
        List<Task> tasks = taskRepository.findAllById(taskDistribution.getTaskIdList());
        tasks.forEach(t -> t.setDeadline(taskDistribution.getDeadline()));

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

        return tasks;
    }

    @Override
    public List<TimeEntryInfo> getEntries(Long id) {
        return timeEntryRepository.findAllByTaskId(id, TimeEntryInfo.class);
    }

    @Override
    @Transactional
    public void closeTask(Long id) {
        taskRepository.findById(id)
                .ifPresent(t -> t.setTaskStatus(TaskStatus.CLOSED));
    }

    private List<TaskListInfo> getTaskListByUserAndStatus(User user, TaskStatus status) {
        if (user.getPosition().equals(Position.EMPLOYEE)) {
            return taskRepository.getTaskByAssigneeAndTaskStatus(user, status);
        } else if (user.getPosition().equals(Position.PROJECT_SUPERVISOR)) {
            List<Long> projectIdList = projectService.getProjects(user).stream()
                    .map(ProjectInfo::getId)
                    .collect(Collectors.toUnmodifiableList());
            return taskRepository.findAllByProjectIdInAndTaskStatus(projectIdList, status);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public TaskListInfo getTask(long taskId, User user) {
        return taskRepository.findTaskById(taskId)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<TaskListInfo> getTasksForProject(Long id) {
        return taskRepository.getTaskByProjectId(id);
    }


    @Scheduled(cron = "0 0 2 * * MON-FRI")
    public void setAbnormalTasks() {
        taskRepository.setAbnormalTasks();
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
                    .sum() + task.getEstimateCalculated() * user.getGrade().getCoefficient();
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

        users.stream().min(Comparator.comparing(User::getTimeRemain)).ifPresent(user -> {
                    user.getTasks().add(task);
                    task.setAssignee(user);
                    task.setEstimate(task.getEstimateCalculated() * user.getGrade().getCoefficient());
                });
    }
}
