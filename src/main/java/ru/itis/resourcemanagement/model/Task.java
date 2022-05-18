package ru.itis.resourcemanagement.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

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
    private Double unitValue;

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

    private LocalDateTime deadline;

    private Integer recommendedOrder;

    @ManyToOne
    private TaskSet taskSet;
}
