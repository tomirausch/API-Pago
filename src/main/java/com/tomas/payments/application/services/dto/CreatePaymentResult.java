package com.tomas.payments.application.services.dto;

import com.tomas.payments.domain.model.Payment;

public class CreatePaymentResult {
    private final Payment payment;
    private final boolean isNew;

    public CreatePaymentResult(Payment payment, boolean isNew) {
        this.payment = payment;
        this.isNew = isNew;
    }

    public Payment getPayment() {
        return payment;
    }

    public boolean isNew() {
        return isNew;
    }
}
