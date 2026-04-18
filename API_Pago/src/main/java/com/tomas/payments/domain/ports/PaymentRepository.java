package com.tomas.payments.domain.ports;

import java.util.Optional;

import com.tomas.payments.domain.model.Payment;


public interface PaymentRepository{
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    Payment save(Payment payment);
}
