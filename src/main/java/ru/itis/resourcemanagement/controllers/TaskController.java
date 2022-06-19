package ru.itis.resourcemanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.resourcemanagement.dto.DistributedTaskDto;
import ru.itis.resourcemanagement.dto.TaskDistributionDto;
import ru.itis.resourcemanagement.dto.TimeEntryDto;
import ru.itis.resourcemanagement.dto.UserBriefDto;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.dto.projections.TimeEntryInfo;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TimeEntry;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Void> createTask(@RequestBody Task task) {
        taskService.createTask(task);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskListInfo> getTask(@PathVariable long taskId,
                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTask(taskId, user));
    }

    @GetMapping("/{id}/timeEntries")
    public List<TimeEntryInfo> getTimeEntries(@PathVariable Long id){
        return taskService.getEntries(id);
    }

    @PostMapping("/{taskId}/track")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<TimeEntryDto> track(@PathVariable long taskId,
                                              @AuthenticationPrincipal User user,
                                              @RequestBody TimeEntryDto timeEntry) {
        TimeEntry created = taskService.track(taskId, timeEntry, user);
        TimeEntryDto dto = modelMapper.map(created, TimeEntryDto.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(params = "!closed")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TaskListInfo>> getTasks(@AuthenticationPrincipal User user) {
        List<TaskListInfo> tasksForUser = taskService.getTasksForUser(user);
        return ResponseEntity.ok(tasksForUser);
    }

    @GetMapping(params = "closed")
    public ResponseEntity<List<TaskListInfo>> getClosedTasks(@AuthenticationPrincipal User user) {
        List<TaskListInfo> tasksForUser = taskService.getClosedTasksForUser(user);
        return ResponseEntity.ok(tasksForUser);
    }

    @PostMapping("/{id}/close")
    public void close(@PathVariable Long id){
        taskService.closeTask(id);
    }

    @PostMapping("/distribute")
    public List<DistributedTaskDto> distribute(@RequestBody TaskDistributionDto taskDistribution) {
        return taskService.distribute(taskDistribution).stream()
                .map(t -> new DistributedTaskDto(t.getId(), t.getAssignee() == null ? null :
                        new UserBriefDto(
                                t.getAssignee().getId(),
                                t.getAssignee().getFirstName(),
                                t.getAssignee().getLastName(),
                                t.getAssignee().getMiddleName(),
                                t.getAssignee().getPosition()
                        ), t.getEstimate()))
                .collect(Collectors.toList());
    }
}
