package ru.itis.resourcemanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.UserService;
import ru.itis.resourcemanagement.services.impl.AuthService;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final AuthService authService;

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
