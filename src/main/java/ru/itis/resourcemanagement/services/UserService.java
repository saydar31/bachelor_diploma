package ru.itis.resourcemanagement.services;

import ru.itis.resourcemanagement.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
}
