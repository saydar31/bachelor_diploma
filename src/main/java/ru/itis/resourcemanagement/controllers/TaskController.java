package ru.itis.resourcemanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.resourcemanagement.dto.TaskDto;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.services.TaskService;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<TaskDto> addTask(TaskDto taskDto) {
        Task task = taskService.addTask(taskDto);
        return ResponseEntity.ok(modelMapper.map(task, TaskDto.class));
    }
}
