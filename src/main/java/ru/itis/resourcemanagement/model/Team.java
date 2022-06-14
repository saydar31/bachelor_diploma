package ru.itis.resourcemanagement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    private User supervisor;

    @ManyToMany
    @JoinTable(
            name = "team_to_user",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    @ManyToMany
    private Set<TaskType> taskTypes;

    public void setMembers(List<User> users){
        if (members == null){
            members = new HashSet<>();
        }
        members.clear();
        members.addAll(users);
        members.forEach(member -> member.getTeams().add(this));
    }
}