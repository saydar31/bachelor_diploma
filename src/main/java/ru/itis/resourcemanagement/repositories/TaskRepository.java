package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itis.resourcemanagement.dto.projections.ChartData;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByType(TaskType taskType);

    List<TaskListInfo> getTaskByAssignee(User user);

    List<TaskListInfo> findAllByProjectIdIn(Collection<Long> idList);

    Optional<TaskListInfo> findTaskByIdAndAssignee(Long id, User assignee);

    List<TaskListInfo> getTaskByProjectId(Long projectId);

    @Modifying
    @Query(value = "update task " +
            "set abnormal = true " +
            "where id in (select t.id " +
            "            from task t " +
            "                     inner join task_type_statistics tts on type_id = tts.id " +
            "            where abs(((t.fact_time - tts.constant_bias) / t.square) - tts.e) >= 3 * tts.e)", nativeQuery = true)
    void setAbnormalTasks();

    List<TaskListInfo> findTaskByTypeAndAbnormalTrue(TaskType type);

    <T> List<T> findAllByTaskStatusOrderBySquare(TaskStatus taskStatus, Class<T> projection);
}