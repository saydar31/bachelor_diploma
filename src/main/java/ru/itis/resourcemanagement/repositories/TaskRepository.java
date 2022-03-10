package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.resourcemanagement.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}