package com.tomas.payments.infrastructure.adapters.input.rest.exceptions;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.tomas.payments.application.exceptions.PaymentPersistenceException;
import com.tomas.payments.domain.exceptions.DuplicateIdempotencyKeyException;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateIdempotencyKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateIdempotencyKey(DuplicateIdempotencyKeyException e,
            WebRequest request) {

        logger.warn("Duplicate idempotency key. Path: {} - Message: {}", extractPath(request), e.getMessage());
        
        ErrorResponse error = new ErrorResponse(
                resolveMessage(ErrorCode.DUPLICATE_IDEMPOTENCY_KEY, null),
                Instant.now(),
                ErrorCode.DUPLICATE_IDEMPOTENCY_KEY,
                null,
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(PaymentPersistenceException.class)
    public ResponseEntity<ErrorResponse> handlePaymentPersistence(PaymentPersistenceException e, WebRequest request) {
        
        logger.error("Payment persistence error. Path: {} - Message: {}", extractPath(request), e.getMessage());
        
        ErrorResponse error = new ErrorResponse(
                resolveMessage(ErrorCode.PAYMENT_PERSISTENCE_ERROR, null),
                Instant.now(),
                ErrorCode.PAYMENT_PERSISTENCE_ERROR,
                null,
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            WebRequest request) {
        
        logger.warn("Validation failed. Path: {} - Message: {}", extractPath(request), e.getMessage());
        
        List<String> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse(
                resolveMessage(ErrorCode.INVALID_REQUEST_PARAMETERS, null),
                Instant.now(),
                ErrorCode.INVALID_REQUEST_PARAMETERS,
                details,
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e, WebRequest request) {
        logger.warn("Invalid argument. Path: {} - message: {}", extractPath(request), e.getMessage());
        ErrorResponse error = new ErrorResponse(
                resolveMessage(ErrorCode.INVALID_ARGUMENT, null),
                Instant.now(),
                ErrorCode.INVALID_ARGUMENT,
                null,
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e, WebRequest request) {
        
        
        logger.error("Unexpected error. path: {}", extractPath(request), e);

        ErrorResponse error = new ErrorResponse(
                resolveMessage(ErrorCode.INTERNAL_ERROR, null),
                Instant.now(),
                ErrorCode.INTERNAL_ERROR,
                null,
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String resolveMessage(ErrorCode code, String fallback) {
        if (fallback != null && !fallback.isBlank()) {
            return fallback;
        }

        return switch (code) {
            case DUPLICATE_IDEMPOTENCY_KEY -> "Payment already exists";
            case PAYMENT_PERSISTENCE_ERROR -> "Error persisting payment";
            case INVALID_REQUEST_PARAMETERS -> "Invalid request parameters";
            case INVALID_ARGUMENT -> "Invalid argument provided";
            case INTERNAL_ERROR -> "Internal server error";
        };
    }

    private String extractPath(WebRequest request) {
        return ((org.springframework.web.context.request.ServletWebRequest) request)
                .getRequest()
                .getRequestURI();
    }
}