package ru.itis.resourcemanagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.TaskDto;
import ru.itis.resourcemanagement.exceptions.BadRequestException;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.repositories.TaskRepository;
import ru.itis.resourcemanagement.repositories.TaskTypeRepository;
import ru.itis.resourcemanagement.services.TaskService;
import ru.itis.resourcemanagement.services.TaskTypeService;

@RequiredArgsConstructor
@Component
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskTypeService taskTypeService;

    @Override
    public Task addTask(TaskDto taskDto) {
        Task task = Task.builder()
                .id(taskDto.getId())
                .square(taskDto.getSquare())
                .build();
        TaskType taskType = taskTypeService.getTaskTypeById(taskDto.getTaskTypeId());
        double estimate = taskType.getManHourPerSquareMeter() * task.getSquare();
        task.setEstimate(estimate);
        task.setType(taskType);
        return taskRepository.save(task);
    }
}
