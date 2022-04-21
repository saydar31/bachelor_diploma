package ru.itis.resourcemanagement.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.LoginDto;
import ru.itis.resourcemanagement.dto.TokenPair;
import ru.itis.resourcemanagement.exceptions.UnauthorizedException;
import ru.itis.resourcemanagement.model.RefreshToken;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.repositories.RefreshTokenRepository;
import ru.itis.resourcemanagement.services.UserService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AuthService {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RefreshTokenRepository refreshTokenRepository;
    private Algorithm algorithm;
    JWTVerifier jwtVerifier;


    private static final String ISSUER = "resource-management";

    public TokenPair login(LoginDto loginDto) {
        User user = userService.findByLogin(loginDto.getLogin())
                .filter(u -> passwordEncoder.matches(loginDto.getPassword(), u.getPassword()))
                .orElseThrow(UnauthorizedException::new);
        String accessToken = JWT.create()
                .withIssuer(ISSUER)
                .withExpiresAt(Date.from(Instant.now().plus(1L, ChronoUnit.HOURS)))
                .withSubject(user.getId().toString())
                .sign(algorithm);
        String refreshToken = UUID.randomUUID().toString() + System.currentTimeMillis();
        refreshTokenRepository.save(new RefreshToken(refreshToken, Instant.now(), user));
        return new TokenPair(accessToken, refreshToken);
    }

    public User decodeToken(String token){
        long id;
        try {
            id = Long.parseLong(jwtVerifier.verify(token).getSubject());
        } catch (NumberFormatException | JWTVerificationException e) {
            throw new UnauthorizedException();
        }
        return userService.findById(id)
                .orElseThrow(UnauthorizedException::new);
    }
}
