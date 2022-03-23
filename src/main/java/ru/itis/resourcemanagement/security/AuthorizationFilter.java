package ru.itis.resourcemanagement.security;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
     private final AuthenticationManager authenticationManager;

    private static final String BEARER = "Bearer ";

    public AuthorizationFilter(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null) {
            String token = authorization.substring(authorization.lastIndexOf(BEARER) + BEARER.length());
            Authentication authentication = new TokenAuthorization(token);
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
        }
        filterChain.doFilter(request, response);
    }
}
