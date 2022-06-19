package ru.itis.resourcemanagement.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDistributionDto {
    private List<Long> taskIdList;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime deadline;
}
