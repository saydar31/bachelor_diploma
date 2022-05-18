package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itis.resourcemanagement.model.TaskType;

import java.util.List;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {

    @Modifying
    @Query(value = "update task_type " +
            "set last_params_update = now(), " +
            "    man_hour_per_unit_draft = mph, " +
            "    constant_bias_draft = bias " +
            "from (select type_id, regr_slope(fact_time, unit_value) as mph, " +
            "             regr_intercept(fact_time, unit_value) as bias " +
            "        from task " +
            "        group by type_id " +
            "    ) as new_drafts " +
            "where new_drafts.type_id = task_type.id",
            nativeQuery = true)
    void updateDraftsByLeastSquares();

    @Query("select distinct(tt) from TaskType tt left join fetch tt.taskTypeProperties")
    List<TaskType> getTypesWithProperties();
}