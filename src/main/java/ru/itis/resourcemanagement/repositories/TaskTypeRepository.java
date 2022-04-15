package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itis.resourcemanagement.model.TaskType;

import java.util.List;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {

    @Modifying
    @Query(value = "update task_type\n" +
            "set last_params_update = now(),\n" +
            "    man_hour_per_square_meter_draft = mph,\n" +
            "    constant_bias_draft = bias\n" +
            "from (select type_id, regr_slope(fact_time, square) as mph,\n" +
            "             regr_intercept(fact_time, square) as bias\n" +
            "        from task\n" +
            "        group by type_id\n" +
            "    ) as new_drafts\n" +
            "where new_drafts.type_id = task_type.id",
            nativeQuery = true)
    void updateDraftsByLeastSquares();

    @Query("select distinct(tt) from TaskType tt left join fetch tt.taskTypeProperties")
    List<TaskType> getTypesWithProperties();
}