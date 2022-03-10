package ru.itis.resourcemanagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.exceptions.BadRequestException;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.repositories.TaskTypeRepository;
import ru.itis.resourcemanagement.services.TaskTypeService;
@Component
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {

    private static final String NOT_EXISTS = "NOT_EXISTS";
    private final TaskTypeRepository taskTypeRepository;

    @Override
    public TaskType getTaskTypeById(Long taskTypeId) {
        return taskTypeRepository.findById(taskTypeId)
                .orElseThrow(() -> new BadRequestException("taskTypeId", NOT_EXISTS));
    }
}
