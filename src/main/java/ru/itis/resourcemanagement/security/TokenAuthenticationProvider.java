package ru.itis.resourcemanagement.security;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.impl.AuthService;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final AuthService authService;

    public TokenAuthenticationProvider(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = authService.decodeToken(authentication.getCredentials().toString());
        return new TokenAuthorization(authentication.getCredentials().toString(), user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(TokenAuthorization.class);
    }
}
