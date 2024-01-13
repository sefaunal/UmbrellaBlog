package com.sefaunal.umbrellagateway.Handler;

import com.sefaunal.umbrellagateway.Exception.InvalidAccessException;
import com.sefaunal.umbrellagateway.Exception.InvalidTokenFormatException;
import com.sefaunal.umbrellagateway.Response.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author github.com/sefaunal
 * @since 2024-01-09
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericResponse> handleNotFoundException(NotFoundException ex) {
        GenericResponse genericResponse = new GenericResponse(404, ex.getMessage());

        printErrorLog(genericResponse);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<GenericResponse> handleServiceUnavailableException(ServiceUnavailableException ex) {
        GenericResponse genericResponse = new GenericResponse(503, ex.getMessage());

        printErrorLog(genericResponse);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(genericResponse);
    }
    
    @ExceptionHandler(InvalidAccessException.class)
    public ResponseEntity<GenericResponse> handleInvalidAccessException(InvalidAccessException ex) {
        GenericResponse genericResponse = new GenericResponse(403, ex.getMessage());

        printErrorLog(genericResponse);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(genericResponse);
    }

    @ExceptionHandler(InvalidTokenFormatException.class)
    public ResponseEntity<GenericResponse> handleInvalidTokenFormatException(InvalidTokenFormatException ex) {
        GenericResponse genericResponse = new GenericResponse(400, ex.getMessage());

        printErrorLog(genericResponse);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(genericResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleGeneralException(Exception ex) {
        GenericResponse genericResponse = new GenericResponse(500, ex.getMessage());

        printErrorLog(genericResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
    }
    
    private static void printErrorLog(GenericResponse genericResponse) {
        LOG.error("Error -> Status: {}, Message: {}", genericResponse.getStatus(), genericResponse.getMessage());
    }
}