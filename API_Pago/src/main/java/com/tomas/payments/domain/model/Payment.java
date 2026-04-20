package com.tomas.payments.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    private UUID id;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String idempotencyKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static Payment create(String idempotencyKey, BigDecimal amount, String currency) {
        return Payment.builder()
            .idempotencyKey(idempotencyKey)
            .amount(amount)
            .currency(currency)
            .status(PaymentStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

}
