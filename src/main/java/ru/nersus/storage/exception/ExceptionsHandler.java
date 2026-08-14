package ru.nersus.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nersus.storage.dto.ErrorMessageRs;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessageRs UsernameNotFoundException(Exception ex) {

        return new ErrorMessageRs(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessageRs UserAlreadyExistsException(Exception ex) {

        return new ErrorMessageRs(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessageRs BadCredentialsException(Exception ex) {

        return new ErrorMessageRs(ex.getMessage());
    }

}
