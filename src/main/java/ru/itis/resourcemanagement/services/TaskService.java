package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.dto.TaskDto;
import ru.itis.resourcemanagement.dto.TimeEntryDto;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.TimeEntry;
import ru.itis.resourcemanagement.model.User;

import java.util.List;

public interface TaskService {

    void createTask(Task task);

    TimeEntry track(long taskId, TimeEntryDto timeEntry, User user);

    List<Task> findByTaskType(TaskType taskType);

    List<TaskListInfo> getTasksForUser(User user);
}
