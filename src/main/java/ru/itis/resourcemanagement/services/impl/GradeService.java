package ru.itis.resourcemanagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.itis.resourcemanagement.model.Grade;
import ru.itis.resourcemanagement.repositories.GradeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    public List<Grade> getGrades() {
        return gradeRepository.findAll();
    }
}
