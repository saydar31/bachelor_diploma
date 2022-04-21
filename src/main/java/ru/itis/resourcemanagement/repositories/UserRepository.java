package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.resourcemanagement.dto.projections.UserInfo;
import ru.itis.resourcemanagement.model.Position;
import ru.itis.resourcemanagement.model.Team;
import ru.itis.resourcemanagement.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select id, first_name as \"firstName\", last_name as \"lastName\", middle_name as \"middleName\" " +
            "from app_user " +
            "where id not in (select members_id from app_user_teams where teams_id = :#{#team.id}) " +
            "and position = 'EMPLOYEE'"
            , nativeQuery = true)
    List<UserInfo> getAvailableUsers(Team team);

    List<UserInfo> findUsersByPosition(Position position);

    Optional<User> findByLogin(String login);
}