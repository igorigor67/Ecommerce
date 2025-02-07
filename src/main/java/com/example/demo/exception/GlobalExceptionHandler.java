package com.example.demo.exception;

import com.example.demo.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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
        errorDetail.setProperty("description","Resource not found.");
        return errorDetail;
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ProblemDetail handleUserNotLoggedInException(UserNotLoggedInException exception){
        ProblemDetail errorDetails;
        errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(401), exception.getMessage());
        errorDetails.setProperty("description","User must be logged in.");
        return errorDetails;
    }

    @ExceptionHandler(LowProductStockException.class)
    public ProblemDetail handleLowProductStockException(LowProductStockException exception){
        ProblemDetail errorDetails;
        errorDetails = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(500), exception.getMessage());
        errorDetails.setProperty("description","Not enough product stock.");
        return errorDetails;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }

        errorDetail.setProperty("errors", validationErrors);
        return errorDetail;
    }
}
