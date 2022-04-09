package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.resourcemanagement.dto.projections.ProjectInfo;
import ru.itis.resourcemanagement.model.Project;
import ru.itis.resourcemanagement.model.User;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<ProjectInfo> getProjectsBySupervisor(User user);

    Optional<ProjectInfo> findProjectById(Long id);
}