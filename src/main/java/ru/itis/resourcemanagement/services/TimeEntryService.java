package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.TimeEntry;

import java.util.List;

public interface TimeEntryService {
    List<TimeEntry> findByTaskType(TaskType taskType);
}
