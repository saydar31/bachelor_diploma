package ru.itis.resourcemanagement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class TaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private double manHourPerSquareMeter;

    private double constantBias = .0;

    @OneToMany
    private List<TaskTypeProperty> taskTypeProperties;
}
