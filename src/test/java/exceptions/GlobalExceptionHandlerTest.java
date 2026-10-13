
package com.example.artspace.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleDataIntegrityViolation() {
        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Duplicate entry");

        ResponseEntity<String> response =
                handler.handleDataIntegrityViolation(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflicting with existing data", response.getBody());
    }

    @Test
    void shouldHandleGeneralException() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<String> response =
                handler.handleGeneralException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error ocurred", response.getBody());
    }

    @Test
    void shouldHandleBadCredentials() {
        BadCredentialsException exception =
                new BadCredentialsException("Invalid credentials");

        ResponseEntity<String> response =
                handler.handleBadCredentials(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void shouldHandleMissingParameter() {
        MissingServletRequestParameterException exception =
                new MissingServletRequestParameterException("category", "String");

        ResponseEntity<String> response =
                handler.handleMissingParameter(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Missing required parameter: category", response.getBody());
    }

    @Test
    void shouldHandleMaxUploadSize() {
        MaxUploadSizeExceededException exception =
                new MaxUploadSizeExceededException(5 * 1024 * 1024);

        ResponseEntity<String> response =
                handler.handleMaxUploadSize(exception);

        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, response.getStatusCode());
        assertEquals(
                "File is too large. Maximum allowed size is 5 MB.",
                response.getBody()
        );
    }
}
