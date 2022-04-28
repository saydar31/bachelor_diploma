package ru.itis.resourcemanagement.dto;

import lombok.*;
import ru.itis.resourcemanagement.dto.projections.ChartData;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class TaskTypeChartData {
    private List<ChartData> estimates;
    private List<ChartData> facts;
}
