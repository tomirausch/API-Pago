package com.tomas.payments.application.ports.input;

import com.tomas.payments.application.dto.PaymentRequest;

public interface CreatePaymentUseCase {
    void createPayment(PaymentRequest request);
}
