package ru.itis.resourcemanagement.dto.projections;

import java.time.LocalDate;

public interface TimeEntryInfo {
    Long getId();

    double getTime();

    LocalDate getDate();

    String getComment();
}
