package ru.itis.resourcemanagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.projections.ChartData;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.exceptions.BadRequestException;
import ru.itis.resourcemanagement.exceptions.NotFoundException;
import ru.itis.resourcemanagement.model.TaskStatus;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.repositories.TaskRepository;
import ru.itis.resourcemanagement.repositories.TaskTypeRepository;
import ru.itis.resourcemanagement.services.TaskTypeService;

import javax.transaction.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {

    private static final String NOT_EXISTS = "NOT_EXISTS";
    private final TaskTypeRepository taskTypeRepository;
    private final TaskRepository taskRepository;

    @Override
    public TaskType getTaskTypeById(Long taskTypeId) {
        return taskTypeRepository.findById(taskTypeId)
                .orElseThrow(() -> new BadRequestException("taskTypeId", NOT_EXISTS));
    }

    @Override
    public List<TaskType> getTaskTypes() {
        return taskTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void confirmParametersChange(Long id) {
        TaskType taskType = taskTypeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        taskType.updateEstimateParams();
    }

    @Override
    public List<TaskListInfo> getAnomalies(Long id) {
        TaskType taskType = taskTypeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return taskRepository.findTaskByTypeAndAbnormalTrue(taskType);
    }

    @Override
    public List<ChartData> getChartData(Long id) {
        return taskRepository.findAllByTaskStatusAndTypeIdOrderByUnitValue(TaskStatus.CLOSED, id, ChartData.class);
    }

    @Scheduled(cron = "0 0 2 * * MON-FRI")
    public void recomputeStandards() {
        taskTypeRepository.updateDraftsByLeastSquares();
    }

}
