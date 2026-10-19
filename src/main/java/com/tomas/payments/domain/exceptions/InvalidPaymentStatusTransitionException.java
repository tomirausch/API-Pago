package com.tomas.payments.domain.exceptions;

public class InvalidPaymentStatusTransitionException extends RuntimeException {

    public InvalidPaymentStatusTransitionException(String message) {
        super(message);
    }

    public InvalidPaymentStatusTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
