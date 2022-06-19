package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.TimeEntry;

import java.util.List;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {

    @Query("select te from TimeEntry te where te.task.type = ?1")
    List<TimeEntry> findAllByTaskType(TaskType taskType);

    <T> List<T> findAllByTaskId(Long id, Class<T> tClass);
}