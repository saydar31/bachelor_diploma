package ru.itis.resourcemanagement.model.embeded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class TaskCoefficients {
    private double manHourPerUnit = .0;
    private double constantBias = .0;
}
