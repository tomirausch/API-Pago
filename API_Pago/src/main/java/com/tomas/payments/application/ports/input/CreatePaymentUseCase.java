package com.tomas.payments.application.ports.input;

import com.tomas.payments.domain.model.Payment;

public interface CreatePaymentUseCase {
    Payment createPayment(Payment payment);
}
