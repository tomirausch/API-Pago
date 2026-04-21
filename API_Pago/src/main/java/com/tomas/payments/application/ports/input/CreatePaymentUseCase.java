package com.tomas.payments.application.ports.input;

import com.tomas.payments.application.services.dto.CreatePaymentResult;
import com.tomas.payments.domain.model.Payment;

public interface CreatePaymentUseCase {
    CreatePaymentResult createPayment(Payment payment);
}
