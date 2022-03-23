package ru.itis.resourcemanagement.model;

import lombok.*;

import javax.persistence.*;

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

    private String comment;

    @ManyToOne
    private User employee;

    @ManyToOne
    private Task task;



}