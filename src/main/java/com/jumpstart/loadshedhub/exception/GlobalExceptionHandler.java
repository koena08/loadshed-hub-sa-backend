package com.jumpstart.loadshedhub.exception;

import com.jumpstart.loadshedhub.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

//Safety net for entire App
//Catches errors before they crash the server and return formatted response
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Returns 404 Not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        Response<Void> errorResponse = Response.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalStateException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Response<Void>> handleBadRequest(Exception ex) {
        return new ResponseEntity<>(Response.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    //Catches any other expected errors
    //Returns 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGeneralException(Exception ex) {
        Response<Void> errorResponse = Response.error("An unexpected internal server error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
