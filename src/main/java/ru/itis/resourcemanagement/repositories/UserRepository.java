package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.resourcemanagement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}