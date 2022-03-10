package ru.itis.resourcemanagement.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskDto implements Serializable {
    private final Long id;
    private final double factTime;
    private final double estimate;
    private final Double square;
    private final Long taskTypeId;
}
