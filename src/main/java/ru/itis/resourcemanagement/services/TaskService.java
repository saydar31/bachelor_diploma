package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.dto.TaskDistributionDto;
import ru.itis.resourcemanagement.dto.TimeEntryDto;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.dto.projections.TimeEntryInfo;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.TimeEntry;
import ru.itis.resourcemanagement.model.User;

import java.util.Collection;
import java.util.List;

public interface TaskService {

    void createTask(Task task);

    TimeEntry track(long taskId, TimeEntryDto timeEntry, User user);

    List<TaskListInfo> getTasksForUser(User user);

    TaskListInfo getTask(long taskId, User user);

    List<TaskListInfo> getTasksForProject(Long id);

    List<TaskListInfo> getClosedTasksForUser(User user);

    Collection<Task> distribute(TaskDistributionDto taskDistribution);

    List<TimeEntryInfo> getEntries(Long id);

    void closeTask(Long id);
}
