package ru.itis.resourcemanagement.dto.projections;

public interface TeamInfo {
    Long getId();

    String getName();

    UserInfo getSupervisor();

    interface UserInfo {
        Long getId();

        String getFirstName();

        String getLastName();

        String getMiddleName();
    }
}
