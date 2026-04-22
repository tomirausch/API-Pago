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
        if(idempotencyKey == null || idempotencyKey.isEmpty()) {
            throw new IllegalArgumentException("Idempotency key is required");
        }
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if(!currencyValid(currency)) {
            throw new IllegalArgumentException("Invalid currency code");
        }

        LocalDateTime now = LocalDateTime.now();
        return Payment.builder()
            .idempotencyKey(idempotencyKey)
            .amount(amount)
            .currency(currency)
            .status(PaymentStatus.PENDING)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    public void updateStatus(PaymentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status is required");
        }

        if (this.status == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot change a COMPLETED payment");
        }

        if (this.status == PaymentStatus.FAILED) {
            throw new IllegalStateException("Cannot change a FAILED payment");
        }

        if (this.status == PaymentStatus.PENDING) {
            if (newStatus != PaymentStatus.COMPLETED && newStatus != PaymentStatus.FAILED) {
                throw new IllegalArgumentException("Invalid status transition from PENDING");
            }
        }

        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    private static boolean currencyValid(String currency) {
        // Implement basic currency code validation (e.g., ISO 4217)
        return currency != null && currency.matches("^[A-Z]{3}$");
    }

}
