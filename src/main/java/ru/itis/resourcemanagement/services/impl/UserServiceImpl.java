package ru.itis.resourcemanagement.services.impl;

import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.repositories.UserRepository;
import ru.itis.resourcemanagement.services.UserService;

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
}
