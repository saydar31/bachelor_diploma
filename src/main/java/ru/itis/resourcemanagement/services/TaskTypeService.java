package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.model.TaskType;

public interface TaskTypeService {
    TaskType getTaskTypeById(Long taskTypeId);
}
