package ru.itis.resourcemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.resourcemanagement.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}