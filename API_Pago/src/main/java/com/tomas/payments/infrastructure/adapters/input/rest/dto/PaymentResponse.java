package com.tomas.payments.infrastructure.adapters.input.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.tomas.payments.domain.model.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private UUID id;
    private String idempotencyKey;
    private String currency;
    private BigDecimal amount;
    private PaymentStatus status;
}
