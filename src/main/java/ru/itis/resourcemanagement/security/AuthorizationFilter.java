package ru.itis.resourcemanagement.security;

import liquibase.pro.packaged.S;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
     private final AuthenticationManager authenticationManager;

    private static final String BEARER = "Bearer ";
    private static final Object ANONYMOUS_PRINCIPAL = new Object();
    private static final Collection<GrantedAuthority> ANONYMOUS_GRANTED_AUTHORITIES = List.of(new SimpleGrantedAuthority("ANONYMOUS"));

    public AuthorizationFilter(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null) {
            String token = authorization.substring(authorization.lastIndexOf(BEARER) + BEARER.length());
            Authentication authentication = new TokenAuthorization(token);
            try {
                SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
            } catch (AuthenticationException e) {
                SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(authorization, ANONYMOUS_PRINCIPAL, ANONYMOUS_GRANTED_AUTHORITIES));
            }
        }
        filterChain.doFilter(request, response);
    }
}
