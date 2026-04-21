package com.tomas.payments.application.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomas.payments.application.ports.input.GetPaymentUseCase;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.model.Payment;

@Service
public class GetPayment implements GetPaymentUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetPayment.class);
    private final PaymentRepositoryPort paymentRepository;

    public GetPayment(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        logger.debug("Searching for payment with idempotency key: {}", idempotencyKey);
        Optional<Payment> payment = paymentRepository.findByIdempotencyKey(idempotencyKey);
        
        if (payment.isPresent()) {
            logger.info("Payment found for idempotency key: {}. Payment ID: {}, status: {}, amount: {}", 
                idempotencyKey, payment.get().getId(), payment.get().getStatus(), payment.get().getAmount());
        } else {
            logger.warn("Payment not found for idempotency key: {}", idempotencyKey);
        }
        
        return payment;
    }

}
