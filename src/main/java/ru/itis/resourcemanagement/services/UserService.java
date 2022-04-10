package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.dto.projections.TeamInfo;
import ru.itis.resourcemanagement.model.Team;
import ru.itis.resourcemanagement.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);

    List<User> findNewAvailableUsers(Team team);

    List<User> findByIdIn(Collection<Long> userIds);
}
