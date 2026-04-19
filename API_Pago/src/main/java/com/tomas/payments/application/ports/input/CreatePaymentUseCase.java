package com.tomas.payments.application.ports.input;

import com.tomas.payments.domain.model.Payment;

public interface CreatePaymentUseCase {
    void createPayment(Payment payment);
}
