package com.tomas.payments.infrastructure.adapters.input.rest.exceptions;

public enum ErrorCode {
    DUPLICATE_IDEMPOTENCY_KEY,
    PAYMENT_PERSISTENCE_ERROR,
    INVALID_REQUEST_PARAMETERS,
    INVALID_ARGUMENT,
    INTERNAL_ERROR,
    PAYMENT_NOT_FOUND,
}
