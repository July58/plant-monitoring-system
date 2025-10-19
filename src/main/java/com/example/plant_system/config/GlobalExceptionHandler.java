package com.example.plant_system.config;

import com.example.plant_system.exception.NullEntityReferenceException;
import com.example.plant_system.payload.response.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("Entity not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse("Entity not found: " + ex.getMessage()));
    }


    @ExceptionHandler(NullEntityReferenceException.class)
    public ResponseEntity<MessageResponse> handleNullEntityReferenceException(NullEntityReferenceException ex) {
        logger.error("Null entity reference: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Invalid or null entity reference: " + ex.getMessage()));
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<MessageResponse> handleIOException(IOException ex) {
        logger.error("File processing error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("File processing error: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGeneralException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Unexpected server error: " + ex.getMessage()));
    }
}
