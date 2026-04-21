package com.tomas.payments.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomas.payments.application.exceptions.PaymentPersistenceException;
import com.tomas.payments.application.ports.input.CreatePaymentUseCase;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.exceptions.DuplicateIdempotencyKeyException;
import com.tomas.payments.domain.model.Payment;

@Service
public class CreatePayment implements CreatePaymentUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreatePayment.class);
    private final PaymentRepositoryPort paymentRepository;

    public CreatePayment(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment createPayment(Payment payment) {
        String idempotencyKey = payment.getIdempotencyKey();
        logger.info("Attempting to create payment with idempotency key: {}", idempotencyKey);
        
        return paymentRepository.findByIdempotencyKey(idempotencyKey)
            .map(existingPayment -> {
                logger.info("Payment with idempotency key {} already exists. Reusing existing payment with ID: {}", 
                    idempotencyKey, existingPayment.getId());
                return existingPayment;
            })
            .orElseGet(() -> {
                try {
                    logger.debug("No existing payment found. Creating new payment with idempotency key: {}", idempotencyKey);
                    Payment savedPayment = paymentRepository.save(payment);
                    logger.info("Payment successfully created. ID: {}, idempotency key: {}, amount: {}, status: {}", 
                        savedPayment.getId(), idempotencyKey, savedPayment.getAmount(), savedPayment.getStatus());
                    return savedPayment;
                } catch (DuplicateIdempotencyKeyException e) {
                    logger.warn("Duplicate idempotency key detected: {}. Attempting to retrieve existing payment.", idempotencyKey);
                    return paymentRepository.findByIdempotencyKey(idempotencyKey)
                        .orElseThrow(() -> {
                            logger.error("Failed to create payment and no existing payment found with idempotency key: {}", idempotencyKey);
                            return new PaymentPersistenceException("Error saving payment and no existing payment found with idempotency key: " + idempotencyKey);
                        });
                }
            });
    }
}
