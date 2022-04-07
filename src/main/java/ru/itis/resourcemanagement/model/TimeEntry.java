package ru.itis.resourcemanagement.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class TimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private double time;

    private LocalDate date;

    private String comment;

    @ManyToOne
    private User employee;

    @ManyToOne
    private Task task;



}