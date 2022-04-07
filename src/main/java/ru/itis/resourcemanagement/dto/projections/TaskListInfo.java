package ru.itis.resourcemanagement.dto.projections;

import ru.itis.resourcemanagement.model.Position;

public interface TaskListInfo {
    Long getId();

    double getFactTime();

    double getEstimate();

    Double getSquare();

    TaskTypeInfo getType();

    UserInfo getAssignee();

    ProjectInfo getProject();

    interface TaskTypeInfo {
        Long getId();

        String getDescription();
    }

    interface UserInfo {
        Long getId();

        String getFirstName();

        String getLastName();

        String getMiddleName();

        Position getPosition();
    }

    interface ProjectInfo {
        Long getId();

        String getName();
    }
}
