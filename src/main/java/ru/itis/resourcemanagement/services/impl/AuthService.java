package ru.itis.resourcemanagement.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.dto.LoginDto;
import ru.itis.resourcemanagement.dto.TokenPair;
import ru.itis.resourcemanagement.exceptions.NotFoundException;
import ru.itis.resourcemanagement.exceptions.UnauthorizedException;
import ru.itis.resourcemanagement.model.RefreshToken;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.repositories.RefreshTokenRepository;
import ru.itis.resourcemanagement.services.UserService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder,
                       RefreshTokenRepository refreshTokenRepository,
                       @Value("${auth.jwt.secret}") String secret) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.algorithm = Algorithm.HMAC256(secret);
        jwtVerifier = JWT.require(this.algorithm).build();
    }

    private static final String ISSUER = "resource-management";

    public TokenPair login(LoginDto loginDto) {
        User user = userService.findByLogin(loginDto.getLogin())
                .filter(u -> passwordEncoder.matches(loginDto.getPassword(), u.getPassword()))
                .orElseThrow(UnauthorizedException::new);
       return getTokenPair(user);
    }

    private TokenPair getTokenPair(User user){
        String accessToken = JWT.create()
                .withIssuer(ISSUER)
                .withExpiresAt(Date.from(Instant.now().plus(1L, ChronoUnit.HOURS)))
                .withSubject(user.getId().toString())
                .sign(algorithm);
        String refreshToken = UUID.randomUUID().toString() + System.currentTimeMillis();
        refreshTokenRepository.save(new RefreshToken(refreshToken, Instant.now(), user));
        return new TokenPair(accessToken, refreshToken);
    }

    public User decodeToken(String token) {
        long id;
        try {
            id = Long.parseLong(jwtVerifier.verify(token).getSubject());
        } catch (NumberFormatException | JWTVerificationException e) {
            throw new UnauthorizedException();
        }
        return userService.findById(id)
                .orElseThrow(UnauthorizedException::new);
    }

    public TokenPair refresh(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(NotFoundException::new);
        User user = token.getUser();
        refreshTokenRepository.delete(token);
        return getTokenPair(user);
    }
}
