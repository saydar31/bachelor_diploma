package ru.itis.resourcemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DistributedTaskDto {
    private Long id;
    private UserBriefDto assignee;
    private double estimate;
}
