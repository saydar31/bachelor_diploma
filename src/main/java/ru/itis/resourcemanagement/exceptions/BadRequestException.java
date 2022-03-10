package ru.itis.resourcemanagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@ResponseStatus(HttpStatus.BAD_REQUEST)
@AllArgsConstructor
@Getter
public class BadRequestException extends RuntimeException {

    private final String field;
    private final String message;
}
