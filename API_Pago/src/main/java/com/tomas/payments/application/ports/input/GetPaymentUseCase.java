package com.tomas.payments.application.ports.input;

import java.util.Optional;
import java.util.UUID;

import com.tomas.payments.domain.model.Payment;

public interface GetPaymentUseCase {
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    Optional<Payment> findById(UUID paymentId);
}
