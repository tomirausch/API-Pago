package com.tomas.payments.application.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.tomas.payments.application.ports.input.CreatePaymentUseCase;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.model.Payment;

public class CreatePayment implements CreatePaymentUseCase {

    @Autowired
    private PaymentRepositoryPort paymentRepository;

    @Override
    public void createPayment(Payment payment) {
        paymentRepository.findByIdempotencyKey(payment.getIdempotencyKey())
            .orElseGet(() -> paymentRepository.save(payment));
    }
}
