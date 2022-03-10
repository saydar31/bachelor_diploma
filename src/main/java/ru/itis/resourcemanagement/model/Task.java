package ru.itis.resourcemanagement.model;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Task {
    @Id
    private Long id;

    private double factTime = .0;

    private double estimate;

    private Double square;

    @ManyToOne(optional = false)
    private TaskType type;

}
