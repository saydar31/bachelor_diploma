package ru.itis.resourcemanagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.TimeEntryDto;
import ru.itis.resourcemanagement.exceptions.NotFoundException;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.TimeEntry;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.repositories.TaskRepository;
import ru.itis.resourcemanagement.repositories.TimeEntryRepository;
import ru.itis.resourcemanagement.services.TaskService;
import ru.itis.resourcemanagement.services.TaskTypeService;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskTypeService taskTypeService;
    private final TimeEntryRepository timeEntryRepository;

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
                .task(task)
                .employee(user)
                .build();
        return timeEntryRepository.save(timeEntry);
    }

    @Override
    public List<Task> findByTaskType(TaskType taskType) {
        return taskRepository.findAllByType(taskType);
    }
}
