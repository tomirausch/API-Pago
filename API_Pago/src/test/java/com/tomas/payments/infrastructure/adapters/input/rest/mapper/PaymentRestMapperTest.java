package com.tomas.payments.infrastructure.adapters.input.rest.mapper;

import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.domain.model.PaymentStatus;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentRequest;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRestMapperTest {

    @Test
    @DisplayName("Should map PaymentRequest to Payment domain object")
    void shouldMapPaymentRequestToPaymentDomainObject() {
        PaymentRequest request = PaymentRequest.builder()
            .idempotencyKey("key-123")
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .build();

        Payment payment = PaymentRestMapper.toDomain(request);

        assertNotNull(payment);
        assertEquals("key-123", payment.getIdempotencyKey());
        assertEquals(new BigDecimal("100.00"), payment.getAmount());
        assertEquals("USD", payment.getCurrency());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    @DisplayName("Should map Payment domain object to PaymentResponse")
    void shouldMapPaymentDomainObjectToPaymentResponse() {
        Payment payment = Payment.builder()
            .id(java.util.UUID.randomUUID())
            .idempotencyKey("key-123")
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.COMPLETED)
            .build();

        PaymentResponse response = PaymentRestMapper.toResponse(payment);

        assertNotNull(response);
        assertEquals(payment.getId(), response.getId());
        assertEquals("key-123", response.getIdempotencyKey());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertEquals("USD", response.getCurrency());
        assertEquals(PaymentStatus.COMPLETED, response.getStatus());
    }
}
