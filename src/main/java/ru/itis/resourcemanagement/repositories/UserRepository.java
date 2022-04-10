package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.resourcemanagement.model.Team;
import ru.itis.resourcemanagement.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u left join Team t on u member of t.members " +
            "where (t <> :team or t is null) " +
            "and u.position = ru.itis.resourcemanagement.model.Position.EMPLOYEE")
    List<User> getAvailableUsers(Team team);
}