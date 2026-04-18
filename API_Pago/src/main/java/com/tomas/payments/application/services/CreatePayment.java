package com.tomas.payments.application.services;

import com.tomas.payments.application.dto.PaymentRequest;
import com.tomas.payments.application.ports.input.CreatePaymentUseCase;

public class CreatePayment implements CreatePaymentUseCase {
    @Override
    public void createPayment(PaymentRequest request) {
        // Implementación del caso de uso para crear un pago
    }
}
