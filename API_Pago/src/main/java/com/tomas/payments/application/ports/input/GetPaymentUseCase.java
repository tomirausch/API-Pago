package com.tomas.payments.application.ports.input;

import java.util.Optional;

import com.tomas.payments.domain.model.Payment;

public interface GetPaymentUseCase {
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}
