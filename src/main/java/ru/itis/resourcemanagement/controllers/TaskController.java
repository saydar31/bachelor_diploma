package ru.itis.resourcemanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.itis.resourcemanagement.dto.TimeEntryDto;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TimeEntry;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Void> createTask(Task task) {
        taskService.createTask(task);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskListInfo> getTask(@PathVariable long taskId,
                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTask(taskId, user));
    }

    @PostMapping("/{taskId}/track")
    public ResponseEntity<TimeEntryDto> track(@PathVariable long taskId,
                                              @AuthenticationPrincipal User user,
                                              @RequestBody TimeEntryDto timeEntry) {
        TimeEntry created = taskService.track(taskId, timeEntry, user);
        TimeEntryDto dto = modelMapper.map(created, TimeEntryDto.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TaskListInfo>> getTasks(@AuthenticationPrincipal User user) {
        List<TaskListInfo> tasksForUser = taskService.getTasksForUser(user);
        return ResponseEntity.ok(tasksForUser);
    }
}
