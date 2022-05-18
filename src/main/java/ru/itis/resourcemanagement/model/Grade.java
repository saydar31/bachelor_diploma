package ru.itis.resourcemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grade_seq")
    @SequenceGenerator(name = "grade_seq")
    private Long id;

    private String name;

    private double coefficient = 1.0;
}
