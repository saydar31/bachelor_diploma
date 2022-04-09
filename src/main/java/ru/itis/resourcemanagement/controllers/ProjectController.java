package ru.itis.resourcemanagement.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.resourcemanagement.dto.projections.ProjectInfo;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.TaskService;
import ru.itis.resourcemanagement.services.impl.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PROJECT_SUPERVISOR')")
    public ResponseEntity<List<ProjectInfo>> getProjects(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(projectService.getProjects(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjectInfo> getProjectInfo(@PathVariable Long id) {
        return ResponseEntity.of(projectService.getProject(id));
    }

    @GetMapping("/{id}/tasks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TaskListInfo>> getProjectTasks(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksForProject(id));
    }
}
