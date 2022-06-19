package ru.itis.resourcemanagement.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itis.resourcemanagement.dto.TaskTypeChartData;
import ru.itis.resourcemanagement.dto.projections.ChartData;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.services.TaskTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task-type")
public class TaskTypeController {

    private final TaskTypeService taskTypeService;

    public TaskTypeController(TaskTypeService taskTypeService) {
        this.taskTypeService = taskTypeService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PROJECT_SUPERVISOR')")
    public List<TaskType> getTypes() {
        return taskTypeService.getTaskTypes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PROJECT_SUPERVISOR')")
    public TaskType getType(@PathVariable Long id) {
        return taskTypeService.getTaskTypeById(id);
    }

    @PostMapping("/{id}/confirm")
    public void confirmNewParameters(@PathVariable Long id) {
        taskTypeService.confirmParametersChange(id);
    }

    @GetMapping("/{id}/anomalies")
    public List<TaskListInfo> getAnomalies(@PathVariable Long id) {
        return taskTypeService.getAnomalies(id);
    }

    @GetMapping("/{id}/chart-data")
    public List<ChartData> getChartData(@PathVariable Long id) {
        return taskTypeService.getChartData(id);
    }
}
