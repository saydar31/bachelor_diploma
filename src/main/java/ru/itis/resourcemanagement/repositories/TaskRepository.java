package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.resourcemanagement.dto.projections.TaskListInfo;
import ru.itis.resourcemanagement.model.Task;
import ru.itis.resourcemanagement.model.TaskType;
import ru.itis.resourcemanagement.model.User;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByType(TaskType taskType);

    List<TaskListInfo> getTaskByAssignee(User user);
}