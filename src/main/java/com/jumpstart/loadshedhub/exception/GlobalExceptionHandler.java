package com.jumpstart.loadshedhub.exception;

import com.jumpstart.loadshedhub.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;

//Safety net for entire App
//Catches errors before they crash the server and return formatted response
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Returns 404 Not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ResponseDTO<Void> errorResponse = ResponseDTO.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //Catches Spring Security authentication failures (wrong password, bad token)
    //Returns 401 Unauthorized
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleAuthError(AuthenticationException ex) {
        ResponseDTO<Void> errorResponse = ResponseDTO.error("Invalid credentials or authorisation failed");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    //Catches any other expected errors
    //Returns 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleGeneralException(Exception ex) {
        ResponseDTO<Void> errorResponse = ResponseDTO.error("An unexpected internal server error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
