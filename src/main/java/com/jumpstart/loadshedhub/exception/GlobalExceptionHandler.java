package com.jumpstart.loadshedhub.exception;

import com.jumpstart.loadshedhub.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

//Safety net for entire App
//Catches errors before they crash the server and return formatted response
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst().orElse("Request validation failed");
        return new ResponseEntity<>(ResponseDTO.error(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBadRequest(IllegalStateException ex) {
        return new ResponseEntity<>(ResponseDTO.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    //Returns 404 Not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ResponseDTO<Void> errorResponse = ResponseDTO.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //Catches any other expected errors
    //Returns 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleGeneralException(Exception ex) {
        ResponseDTO<Void> errorResponse = ResponseDTO.error("An unexpected internal server error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
