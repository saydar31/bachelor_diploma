package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.dto.TaskDto;
import ru.itis.resourcemanagement.model.Task;

public interface TaskService {
    Task addTask(TaskDto taskDto);
}
