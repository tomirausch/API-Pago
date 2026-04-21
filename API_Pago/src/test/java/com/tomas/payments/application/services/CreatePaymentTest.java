package com.tomas.payments.application.services;

import com.tomas.payments.application.exceptions.PaymentPersistenceException;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.application.services.dto.CreatePaymentResult;
import com.tomas.payments.domain.exceptions.DuplicateIdempotencyKeyException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePaymentTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @InjectMocks
    private CreatePayment createPayment;

    @Test
    @DisplayName("Should create new payment when idempotency key does not exist")
    void shouldCreateNewPaymentWhenIdempotencyKeyDoesNotExist() {
        String idempotencyKey = "key-123";
        Payment payment = Payment.create(idempotencyKey, new BigDecimal("100.00"), "USD");
        Payment savedPayment = Payment.builder()
            .id(UUID.randomUUID())
            .idempotencyKey(idempotencyKey)
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.PENDING)
            .build();

        when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        CreatePaymentResult result = createPayment.createPayment(payment);

        assertTrue(result.isNew());
        assertEquals(savedPayment, result.getPayment());
        verify(paymentRepository).findByIdempotencyKey(idempotencyKey);
        verify(paymentRepository).save(payment);
    }

    @Test
    @DisplayName("Should return existing payment when idempotency key already exists")
    void shouldReturnExistingPaymentWhenIdempotencyKeyAlreadyExists() {
        String idempotencyKey = "key-123";
        Payment payment = Payment.create(idempotencyKey, new BigDecimal("100.00"), "USD");
        Payment existingPayment = Payment.builder()
            .id(UUID.randomUUID())
            .idempotencyKey(idempotencyKey)
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.PENDING)
            .build();

        when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(existingPayment));

        CreatePaymentResult result = createPayment.createPayment(payment);

        assertFalse(result.isNew());
        assertEquals(existingPayment, result.getPayment());
        verify(paymentRepository).findByIdempotencyKey(idempotencyKey);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should handle race condition and return existing payment")
    void shouldHandleRaceConditionAndReturnExistingPayment() {
        String idempotencyKey = "key-123";
        Payment payment = Payment.create(idempotencyKey, new BigDecimal("100.00"), "USD");
        Payment existingPayment = Payment.builder()
            .id(UUID.randomUUID())
            .idempotencyKey(idempotencyKey)
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.COMPLETED)
            .build();

        when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenThrow(new DuplicateIdempotencyKeyException("Duplicate key"));
        when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(existingPayment));

        CreatePaymentResult result = createPayment.createPayment(payment);

        assertFalse(result.isNew());
        assertEquals(existingPayment, result.getPayment());
    }

    @Test
    @DisplayName("Should throw PaymentPersistenceException when save fails and no existing payment found")
    void shouldThrowPaymentPersistenceExceptionWhenSaveFailsAndNoExistingPaymentFound() {
        String idempotencyKey = "key-123";
        Payment payment = Payment.create(idempotencyKey, new BigDecimal("100.00"), "USD");

        when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenThrow(new DuplicateIdempotencyKeyException("Duplicate key"));
        when(paymentRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.empty());

        assertThrows(PaymentPersistenceException.class, () ->
            createPayment.createPayment(payment));
    }
}
