package ru.itis.resourcemanagement.services.impl;

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

import javax.transaction.Transactional;
import java.util.List;
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
        double estimate = taskType.getManHourPerSquareMeter() * task.getSquare();
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
    public void setAbnormalTasks(){
        taskRepository.setAbnormalTasks();
    }
}
