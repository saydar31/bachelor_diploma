package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.resourcemanagement.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
