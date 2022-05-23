package ru.itis.resourcemanagement.model;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    public double getEstimateCalculated(){
        double gradeCoefficient = assignee == null ?
                1.0 : assignee.getGrade().getCoefficient();
        return (type.getConstantBias() + type.getManHourPerUnit() * unitValue) / gradeCoefficient;
    }

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

    @CreatedDate
    private Instant created;

    @LastModifiedDate
    private Instant modified;

    @ManyToMany
    @JoinTable(
            name = "task_dependency",
            joinColumns = @JoinColumn(name = "dependent_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<Task> subtasks;

    @ManyToMany(mappedBy = "subtasks")
    private Set<Task> dependentTasks;

    @Transient
    private int priority;
}
