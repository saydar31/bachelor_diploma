package ru.itis.resourcemanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.resourcemanagement.dto.TimeEntryDto;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TimeEntry;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.TaskService;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Void> createTask(Task task){
        taskService.createTask(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/track")
    public ResponseEntity<TimeEntryDto> track(@PathVariable long taskId,
                                              @AuthenticationPrincipal User user,
                                              TimeEntryDto timeEntry){
        TimeEntry created = taskService.track(taskId, timeEntry, user);
        TimeEntryDto dto = modelMapper.map(created, TimeEntryDto.class);
        return ResponseEntity.ok(dto);
    }
}
