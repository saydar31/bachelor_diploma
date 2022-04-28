package ru.itis.resourcemanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(){
        super("Bad credentials");
    }

    public UnauthorizedException(String msg) {
        super(msg);
    }
}
