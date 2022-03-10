package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.resourcemanagement.model.TaskTypeProperty;

public interface TaskTypePropertyRepository extends JpaRepository<TaskTypeProperty, Long> {
}