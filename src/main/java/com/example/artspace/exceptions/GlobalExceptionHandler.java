package com.example.artspace.exceptions;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity .status(HttpStatus.CONFLICT).body("Conflicting with existing data");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exception){
        return ResponseEntity .status(HttpStatus.INTERNAL_SERVER_ERROR) .body("Unexpected error ocurred");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity .status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParameter(
            MissingServletRequestParameterException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Missing required parameter: " + exception.getParameterName());
    }
}
