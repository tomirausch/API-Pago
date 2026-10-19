package com.tomas.payments.application.services;

import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.exceptions.PaymentNotFoundException;
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
class UpdateStatusPaymentTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @InjectMocks
    private UpdateStatusPayment updateStatusPayment;

    @Test
    @DisplayName("Should update payment status from PENDING to COMPLETED")
    void shouldUpdatePaymentStatusFromPendingToCompleted() {
        UUID paymentId = UUID.randomUUID();
        Payment payment = Payment.builder()
            .id(paymentId)
            .idempotencyKey("key-123")
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.PENDING)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = updateStatusPayment.updateStatus(paymentId, PaymentStatus.COMPLETED);

        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository).save(payment);
    }

    @Test
    @DisplayName("Should update payment status from PENDING to FAILED")
    void shouldUpdatePaymentStatusFromPendingToFailed() {
        UUID paymentId = UUID.randomUUID();
        Payment payment = Payment.builder()
            .id(paymentId)
            .idempotencyKey("key-123")
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.PENDING)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = updateStatusPayment.updateStatus(paymentId, PaymentStatus.FAILED);

        assertEquals(PaymentStatus.FAILED, result.getStatus());
        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository).save(payment);
    }

    @Test
    @DisplayName("Should throw PaymentNotFoundException when payment does not exist")
    void shouldThrowPaymentNotFoundExceptionWhenPaymentDoesNotExist() {
        UUID paymentId = UUID.randomUUID();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () ->
            updateStatusPayment.updateStatus(paymentId, PaymentStatus.COMPLETED));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when updating COMPLETED payment")
    void shouldThrowIllegalStateExceptionWhenUpdatingCompletedPayment() {
        UUID paymentId = UUID.randomUUID();
        Payment payment = Payment.builder()
            .id(paymentId)
            .idempotencyKey("key-123")
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.COMPLETED)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        assertThrows(IllegalStateException.class, () ->
            updateStatusPayment.updateStatus(paymentId, PaymentStatus.FAILED));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when updating FAILED payment")
    void shouldThrowIllegalStateExceptionWhenUpdatingFailedPayment() {
        UUID paymentId = UUID.randomUUID();
        Payment payment = Payment.builder()
            .id(paymentId)
            .idempotencyKey("key-123")
            .amount(new BigDecimal("100.00"))
            .currency("USD")
            .status(PaymentStatus.FAILED)
            .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        assertThrows(IllegalStateException.class, () ->
            updateStatusPayment.updateStatus(paymentId, PaymentStatus.COMPLETED));
    }
}
