package com.tomas.payments.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    @DisplayName("Should create payment with valid data")
    void shouldCreatePaymentWithValidData() {
        Payment payment = Payment.create("key-123", new BigDecimal("100.00"), "USD");

        assertNotNull(payment);
        assertEquals("key-123", payment.getIdempotencyKey());
        assertEquals(new BigDecimal("100.00"), payment.getAmount());
        assertEquals("USD", payment.getCurrency());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertNotNull(payment.getCreatedAt());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw exception when idempotency key is null")
    void shouldThrowExceptionWhenIdempotencyKeyIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
            Payment.create(null, new BigDecimal("100.00"), "USD"));
    }

    @Test
    @DisplayName("Should throw exception when idempotency key is empty")
    void shouldThrowExceptionWhenIdempotencyKeyIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
            Payment.create("", new BigDecimal("100.00"), "USD"));
    }

    @Test
    @DisplayName("Should throw exception when amount is null")
    void shouldThrowExceptionWhenAmountIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
            Payment.create("key-123", null, "USD"));
    }

    @Test
    @DisplayName("Should throw exception when amount is zero")
    void shouldThrowExceptionWhenAmountIsZero() {
        assertThrows(IllegalArgumentException.class, () ->
            Payment.create("key-123", BigDecimal.ZERO, "USD"));
    }

    @Test
    @DisplayName("Should throw exception when amount is negative")
    void shouldThrowExceptionWhenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
            Payment.create("key-123", new BigDecimal("-50.00"), "USD"));
    }

    @Test
    @DisplayName("Should throw exception when currency is null")
    void shouldThrowExceptionWhenCurrencyIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
            Payment.create("key-123", new BigDecimal("100.00"), null));
    }

    @Test
    @DisplayName("Should throw exception when currency is empty")
    void shouldThrowExceptionWhenCurrencyIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
            Payment.create("key-123", new BigDecimal("100.00"), ""));
    }

    @Test
    @DisplayName("Should update status from PENDING to COMPLETED")
    void shouldUpdateStatusFromPendingToCompleted() {
        Payment payment = Payment.create("key-123", new BigDecimal("100.00"), "USD");

        payment.updateStatus(PaymentStatus.COMPLETED);

        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update status from PENDING to FAILED")
    void shouldUpdateStatusFromPendingToFailed() {
        Payment payment = Payment.create("key-123", new BigDecimal("100.00"), "USD");

        payment.updateStatus(PaymentStatus.FAILED);

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when updating COMPLETED payment")
    void shouldThrowExceptionWhenUpdatingCompletedPayment() {
        Payment payment = Payment.create("key-123", new BigDecimal("100.00"), "USD");
        payment.updateStatus(PaymentStatus.COMPLETED);

        assertThrows(IllegalStateException.class, () ->
            payment.updateStatus(PaymentStatus.FAILED));
    }

    @Test
    @DisplayName("Should throw exception when updating FAILED payment")
    void shouldThrowExceptionWhenUpdatingFailedPayment() {
        Payment payment = Payment.create("key-123", new BigDecimal("100.00"), "USD");
        payment.updateStatus(PaymentStatus.FAILED);

        assertThrows(IllegalStateException.class, () ->
            payment.updateStatus(PaymentStatus.COMPLETED));
    }

    @Test
    @DisplayName("Should throw exception when status is null")
    void shouldThrowExceptionWhenStatusIsNull() {
        Payment payment = Payment.create("key-123", new BigDecimal("100.00"), "USD");

        assertThrows(IllegalArgumentException.class, () ->
            payment.updateStatus(null));
    }

    @Test
    @DisplayName("Should throw exception for invalid status transition")
    void shouldThrowExceptionForInvalidStatusTransition() {
        Payment payment = Payment.create("key-123", new BigDecimal("100.00"), "USD");

        assertThrows(IllegalArgumentException.class, () ->
            payment.updateStatus(PaymentStatus.PENDING));
    }
}
