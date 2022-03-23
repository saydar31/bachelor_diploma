package ru.itis.resourcemanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.itis.resourcemanagement.model.User;
import ru.itis.resourcemanagement.services.UserService;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        long id = Long.parseLong(authentication.getCredentials().toString());
        User user = userService.findById(id)
                .orElseThrow(() -> new BadCredentialsException("not found user for token"));
        return new TokenAuthorization(Long.toString(id), user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(TokenAuthorization.class);
    }
}
