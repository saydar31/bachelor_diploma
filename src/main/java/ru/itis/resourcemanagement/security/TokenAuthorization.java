package ru.itis.resourcemanagement.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.itis.resourcemanagement.model.User;

import java.util.Collection;
import java.util.List;


@Getter
public class TokenAuthorization implements Authentication {
    private final User user;
    private final String token;

    public TokenAuthorization(String token) {
        this.token = token;
        this.user = null;
    }

    public TokenAuthorization(String token, User user) {
        this.user = user;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user == null ?
                List.of()
                : List.of(new SimpleGrantedAuthority(user.getPosition().toString()));
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return user != null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return "USER";
    }
}
