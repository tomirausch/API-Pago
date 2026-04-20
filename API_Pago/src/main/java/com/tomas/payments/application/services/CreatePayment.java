package com.tomas.payments.application.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomas.payments.application.exceptions.PaymentPersistenceException;
import com.tomas.payments.application.ports.input.CreatePaymentUseCase;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.exceptions.DuplicateIdempotencyKeyException;
import com.tomas.payments.domain.model.Payment;

@Service
public class CreatePayment implements CreatePaymentUseCase {

    private final PaymentRepositoryPort paymentRepository;

    public CreatePayment(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment createPayment(Payment payment) {
        return paymentRepository.findByIdempotencyKey(payment.getIdempotencyKey())
            .orElseGet(() -> {
                try {
                    return paymentRepository.save(payment);
                } catch (DuplicateIdempotencyKeyException e) {
                    return paymentRepository.findByIdempotencyKey(payment.getIdempotencyKey())
                        .orElseThrow(() -> new PaymentPersistenceException("Error saving payment and no existing payment found with idempotency key: " + payment.getIdempotencyKey()));
                }
            });
    }
}
