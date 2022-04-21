package ru.itis.resourcemanagement.services.impl;

import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.projections.UserInfo;
import ru.itis.resourcemanagement.model.Position;
import ru.itis.resourcemanagement.model.Team;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.repositories.UserRepository;
import ru.itis.resourcemanagement.services.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserInfo> findNewAvailableUsers(Team team) {
        return userRepository.getAvailableUsers(team);
    }

    @Override
    public List<User> findByIdIn(Collection<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    @Override
    public List<UserInfo> getEmployees() {
        return userRepository.findUsersByPosition(Position.EMPLOYEE);
    }

    @Override
    public List<UserInfo> getTeamSupervisors() {
        return userRepository.findUsersByPosition(Position.TEAM_SUPERVISOR);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
