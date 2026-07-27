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

    // Previously unhandled: fell through to the generic 500 handler and hid the real error.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ResponseDTO.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ResponseDTO<Void>> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        return new ResponseEntity<>(ResponseDTO.error("You do not have permission to perform this action."), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleAuthentication(org.springframework.security.core.AuthenticationException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Authentication is required for this action."), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMissingParam(org.springframework.web.bind.MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Missing required parameter: " + ex.getParameterName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO<Void>> handleUnreadableBody(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Request body is missing or malformed JSON."), HttpStatus.BAD_REQUEST);
    }

    //Returns 404 Not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ResponseDTO<Void> errorResponse = ResponseDTO.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex) {
        return new ResponseEntity<>(ResponseDTO.error("This item can't be deleted because it is still referenced elsewhere (e.g. a hub still uses it)."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(org.hibernate.LazyInitializationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleLazyInit(org.hibernate.LazyInitializationException ex) {
        // This means some entity field is being serialized after its Hibernate session
        // closed (open-in-view is disabled). The real fix is to mark that association
        // @JsonIgnore or fetch it eagerly - this handler just keeps the failure diagnosable
        // instead of a raw stack trace reaching the client.
        return new ResponseEntity<>(ResponseDTO.error("A server data-loading error occurred. Please try again or contact support."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Catches any other expected errors
    //Returns 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleGeneralException(Exception ex) {
        ResponseDTO<Void> errorResponse = ResponseDTO.error("An unexpected internal server error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
