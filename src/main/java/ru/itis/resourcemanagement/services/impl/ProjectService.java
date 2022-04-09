package ru.itis.resourcemanagement.services.impl;

import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.projections.ProjectInfo;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.repositories.ProjectRepository;
import ru.itis.resourcemanagement.services.TaskService;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository, TaskService taskService) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectInfo> getProjects(User user) {
        return projectRepository.getProjectsBySupervisor(user);
    }

    public Optional<ProjectInfo> getProject(Long id) {
        return projectRepository.findProjectById(id);
    }
}
