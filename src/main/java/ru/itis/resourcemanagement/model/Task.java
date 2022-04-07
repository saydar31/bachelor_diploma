package ru.itis.resourcemanagement.model;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Project project;
}
