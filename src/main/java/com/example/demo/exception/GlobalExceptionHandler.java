package com.example.demo.exception;

import com.example.demo.exception.user.ContactNumberAlreadyInUseException;
import com.example.demo.exception.user.EmailAlreadyInUseException;
import com.example.demo.exception.user.ResourceNotFoundException;
import com.example.demo.exception.user.UsernameAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnknownException(Exception exception){
        ProblemDetail errorDetail;

        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(500),exception.getMessage());
        errorDetail.setProperty("description","Unknown internal server error.");

        return errorDetail;
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ProblemDetail handleEmailAlreadyInUseException(EmailAlreadyInUseException exception){
        ProblemDetail errorDetail;

        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409),exception.getMessage());
        errorDetail.setProperty("description", "Email already in use.");

        return errorDetail;
    }

    @ExceptionHandler(ContactNumberAlreadyInUseException.class)
    public ProblemDetail handleContactNumberAlreadyInUseException(ContactNumberAlreadyInUseException exception){
        ProblemDetail errorDetail;

        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(409), exception.getMessage());
        errorDetail.setProperty("description","Contact number already in use.");

        return errorDetail;
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    public ProblemDetail handleUsernameAlreadyInUseException(UsernameAlreadyInUseException exception){
        ProblemDetail errorDetail;

        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), exception.getMessage());
        errorDetail.setProperty("description","Username already in use.");

        return errorDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException exception){
        ProblemDetail errorDetail;

        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());
        errorDetail.setProperty("description","Resource not found");

        return errorDetail;
    }
}
