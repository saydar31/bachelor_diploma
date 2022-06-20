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

    List<TaskListInfo> getTaskByAssigneeAndTaskStatus(User user, TaskStatus status);

    List<TaskListInfo> findAllByProjectIdInAndTaskStatus(Collection<Long> idList, TaskStatus status);

    Optional<TaskListInfo> findTaskByIdAndAssignee(Long id, User assignee);
    Optional<TaskListInfo> findTaskById(Long id);

    List<TaskListInfo> getTaskByProjectId(Long projectId);

    /*
        create or replace view task_type_statistics(type_id, sigma_a, e_a, sigma_b, e_b) as
        select tt.id as                                                                type_id,
               stddev((fact_time / g.coefficient - tt.constant_bias) / t.unit_value)   sigma_a,
               avg((fact_time / g.coefficient - tt.constant_bias) / t.unit_value)      e_a,
               stddev(fact_time / g.coefficient - tt.man_hour_per_unit * t.unit_value) sigma_b,
               avg(fact_time / g.coefficient - tt.man_hour_per_unit * t.unit_value)    e_b
        from task_type tt
                 inner join task t on tt.id = t.type_id
                 inner join app_user au on au.id = t.assignee_id
                 inner join grade g on au.grade_id = g.id
        group by tt.id;
     */


    @Modifying
    @Query(value = "update task " +
            "set abnormal = true " +
            "where id in (select t.id " +
            "             from task_type tt " +
            "                      inner join task t on tt.id = t.type_id " +
            "                      inner join app_user au on au.id = t.assignee_id " +
            "                      inner join grade g on au.grade_id = g.id " +
            "                      inner join task_type_statistics tts on tts.type_id = tt.id " +
            "             where abs(tts.e_a - ((fact_time / g.coefficient - tt.constant_bias) / t.unit_value)) > 3 * tts.sigma_a " +
            "                or abs(tts.e_b - fact_time / g.coefficient - tt.man_hour_per_unit * t.unit_value) > 3 * tts.sigma_b)", nativeQuery = true)
    void setAbnormalTasks();

    List<TaskListInfo> findTaskByTypeAndAbnormalTrue(TaskType type);

    <T> List<T> findAllByTaskStatusAndTypeIdOrderByUnitValue(TaskStatus taskStatus, Long id, Class<T> projection);

    List<Task> findByAssigneeAndTaskStatus(User assignee, TaskStatus taskStatus);
}