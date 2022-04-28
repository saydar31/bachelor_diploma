package ru.itis.resourcemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itis.resourcemanagement.dto.projections.UserInfo;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User implements UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String middleName;
    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
    private Position position;

    @OneToMany
    private List<Task> tasks;

    @ManyToMany
    private Set<Team> teams;

    @OneToMany(mappedBy = "supervisor")
    private Set<Project> projects;
}