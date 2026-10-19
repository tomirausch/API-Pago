package com.tomas.payments.application.ports.output;

import java.util.Optional;
import java.util.UUID;

import com.tomas.payments.domain.model.Payment;


public interface PaymentRepositoryPort{
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    Payment save(Payment payment);
    Optional<Payment> findById(UUID paymentId);
}
