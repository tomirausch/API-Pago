package com.tomas.payments.application.services;

import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.domain.model.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPaymentTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @InjectMocks
    private GetPayment getPayment;

    @Test
    @DisplayName("Should find payment by idempotency key")
    void shouldFindPaymentByIdempotencyKey() {
        String idempotencyKey = "key-123";
        Payment payment = Payment.builder()
            .id(UUID.randomUUID())
            .idempotencyKey(idempotencyKey)
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.PENDING)
            .build();

        when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(payment));

        Optional<Payment> result = getPayment.findByIdempotencyKey(idempotencyKey);

        assertTrue(result.isPresent());
        assertEquals(idempotencyKey, result.get().getIdempotencyKey());
        verify(paymentRepository).findByIdempotencyKey(idempotencyKey);
    }

    @Test
    @DisplayName("Should return empty when payment not found by idempotency key")
    void shouldReturnEmptyWhenPaymentNotFoundByIdempotencyKey() {
        String idempotencyKey = "key-non-existent";

        when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());

        Optional<Payment> result = getPayment.findByIdempotencyKey(idempotencyKey);

        assertFalse(result.isPresent());
        verify(paymentRepository).findByIdempotencyKey(idempotencyKey);
    }

    @Test
    @DisplayName("Should find payment by ID")
    void shouldFindPaymentById() {
        UUID paymentId = UUID.randomUUID();
        Payment payment = Payment.builder()
            .id(paymentId)
            .idempotencyKey("key-123")
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.PENDING)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        Optional<Payment> result = getPayment.findById(paymentId);

        assertTrue(result.isPresent());
        assertEquals(paymentId, result.get().getId());
        verify(paymentRepository).findById(paymentId);
    }

    @Test
    @DisplayName("Should return empty when payment not found by ID")
    void shouldReturnEmptyWhenPaymentNotFoundById() {
        UUID paymentId = UUID.randomUUID();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        Optional<Payment> result = getPayment.findById(paymentId);

        assertFalse(result.isPresent());
        verify(paymentRepository).findById(paymentId);
    }
}
