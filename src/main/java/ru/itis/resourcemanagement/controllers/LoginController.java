package ru.itis.resourcemanagement.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itis.resourcemanagement.dto.LoginDto;
import ru.itis.resourcemanagement.dto.TokenPair;
import ru.itis.resourcemanagement.services.impl.AuthService;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<TokenPair> login(@RequestBody LoginDto loginDto) {
        TokenPair tokenPair = authService.login(loginDto);
        return ResponseEntity.ok(tokenPair);
    }

    @GetMapping("/{refreshToken}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<TokenPair> refresh(@PathVariable String refreshToken){
        TokenPair tokenPair = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokenPair);
    }
}
