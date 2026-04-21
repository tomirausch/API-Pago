package com.tomas.payments.application.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomas.payments.application.ports.input.UpdatePaymentStatusUseCase;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.exceptions.PaymentNotFoundException;
import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.domain.model.PaymentStatus;

@Service
@Transactional
public class UpdateStatusPayment implements UpdatePaymentStatusUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateStatusPayment.class);
    private final PaymentRepositoryPort paymentRepository;

    public UpdateStatusPayment(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment updateStatus(UUID paymentId, PaymentStatus newStatus) {
        logger.info("Updating payment status. Payment ID: {}, New Status: {}", paymentId, newStatus);

        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));

        payment.updateStatus(newStatus);
        
        Payment updatedPayment = paymentRepository.save(payment);
        logger.info("Payment status updated successfully. Payment ID: {}, New Status: {}", updatedPayment.getId(), updatedPayment.getStatus());
        return updatedPayment;
    }

}
