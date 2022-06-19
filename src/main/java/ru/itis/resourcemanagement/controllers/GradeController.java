package ru.itis.resourcemanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.resourcemanagement.model.Grade;
import ru.itis.resourcemanagement.services.impl.GradeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @GetMapping
    public List<Grade> getGrades(){
        return gradeService.getGrades();
    }
}
