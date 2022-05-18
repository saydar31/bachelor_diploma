package ru.itis.resourcemanagement.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
public class TaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private double manHourPerUnit;

    private double constantBias = .0;

    private double manHourPerUnitDraft;

    private double constantBiasDraft = .0;

    @OneToMany
    private List<TaskTypeProperty> taskTypeProperties;

    private LocalDateTime lastParamsUpdate;

    public void updateEstimateParams() {
        manHourPerUnit = manHourPerUnitDraft;
        constantBias = constantBiasDraft;
    }
}
