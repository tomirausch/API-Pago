package com.tomas.payments.application.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomas.payments.application.ports.input.GetPaymentUseCase;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.model.Payment;

@Service
public class GetPayment implements GetPaymentUseCase {

    private final PaymentRepositoryPort paymentRepository;

    public GetPayment(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return paymentRepository.findByIdempotencyKey(idempotencyKey);
    }

}
