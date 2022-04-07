package ru.itis.resourcemanagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.exceptions.BadRequestException;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.TimeEntry;
import ru.itis.resourcemanagement.repositories.TaskTypeRepository;
import ru.itis.resourcemanagement.services.TaskService;
import ru.itis.resourcemanagement.services.TaskTypeService;
import ru.itis.resourcemanagement.services.TimeEntryService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {

    private static final String NOT_EXISTS = "NOT_EXISTS";
    private final TaskTypeRepository taskTypeRepository;
    private final TaskService taskService;

    @Override
    public TaskType getTaskTypeById(Long taskTypeId) {
        return taskTypeRepository.findById(taskTypeId)
                .orElseThrow(() -> new BadRequestException("taskTypeId", NOT_EXISTS));
    }

    @Scheduled(cron = "0 0 2 * * MON-FRI")
    public void recomputeStandards() {
        taskTypeRepository.updateDraftsByLeastSquares();
    }

}
