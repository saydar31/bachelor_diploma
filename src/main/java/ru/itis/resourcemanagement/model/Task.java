package ru.itis.resourcemanagement.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Task {
    @Id
    private Long id;

    private double factTime = .0;

    private double estimate;

    @Min(0)
    private Double square;

    @ManyToOne(optional = false)
    private TaskType type;

    @ManyToOne
    private User assignee;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Project project;

    private boolean abnormal;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
}
