package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.TimeEntry;

public interface TaskTypeService {
    TaskType getTaskTypeById(Long taskTypeId);

}
