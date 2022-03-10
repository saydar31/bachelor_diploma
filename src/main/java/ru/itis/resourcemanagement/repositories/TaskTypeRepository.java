package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.resourcemanagement.model.TaskType;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {
}