package ru.itis.resourcemanagement.model;

import lombok.*;
import ru.itis.resourcemanagement.model.embeded.TaskCoefficients;

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

    @Embedded
    private TaskCoefficients coefficients;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "manHourPerUnit", column = @Column(name = "man_hour_per_unit_draft")),
            @AttributeOverride(name="constantBias", column = @Column(name = "constant_bias_draft"))
    })
    private TaskCoefficients coefficientsDraft;

    private LocalDateTime lastParamsUpdate;

    public void updateEstimateParams() {
    }
}
