package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.dto.TaskTypeChartData;
import ru.itis.resourcemanagement.dto.projections.ChartData;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.dto.projections.TaskTypeInfo;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.TimeEntry;

import java.util.List;

public interface TaskTypeService {
    TaskType getTaskTypeById(Long taskTypeId);

    List<TaskType> getTaskTypes();

    void confirmParametersChange(Long id);

    List<TaskListInfo> getAnomalies(Long id);

    List<ChartData> getChartData(Long id);
}
