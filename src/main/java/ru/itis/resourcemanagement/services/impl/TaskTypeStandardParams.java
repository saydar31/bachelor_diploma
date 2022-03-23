package ru.itis.resourcemanagement.services.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskTypeStandardParams {
    private double manHourPerSquareMeter;

    private double constantBias = .0;
}
