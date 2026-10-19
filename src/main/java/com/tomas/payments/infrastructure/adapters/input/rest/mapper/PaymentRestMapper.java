package com.tomas.payments.infrastructure.adapters.input.rest.mapper;

import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentRequest;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentResponse;

public class PaymentRestMapper {

    public static Payment toDomain(PaymentRequest request) {
        return Payment.create(
            request.getIdempotencyKey(),
            request.getAmount(),
            request.getCurrency()
        );
    }

    public static PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
            .id(payment.getId())
            .idempotencyKey(payment.getIdempotencyKey())
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .status(payment.getStatus())
            .build();
    }
}