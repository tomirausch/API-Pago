package com.tomas.payments.application.services;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomas.payments.application.ports.input.GetPaymentUseCase;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.model.Payment;

@Service
@Transactional(readOnly = true)
public class GetPayment implements GetPaymentUseCase {

    private static final Logger logger = LoggerFactory.getLogger(GetPayment.class);
    private final PaymentRepositoryPort paymentRepository;

    public GetPayment(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
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

    @Override
    public Optional<Payment> findById(UUID paymentId) {
        logger.debug("Searching for payment with ID: {}", paymentId);
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        
        if (payment.isPresent()) {
            logger.info("Payment found for ID: {}. Idempotency Key: {}, status: {}, amount: {}", 
                paymentId, payment.get().getIdempotencyKey(), payment.get().getStatus(), payment.get().getAmount());
        } else {
            logger.warn("Payment not found for ID: {}", paymentId);
        }
        
        return payment;
    }

}
