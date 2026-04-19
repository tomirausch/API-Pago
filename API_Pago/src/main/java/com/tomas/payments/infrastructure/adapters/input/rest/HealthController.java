package com.tomas.payments.infrastructure.adapters.input.rest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.domain.model.PaymentStatus;
import com.tomas.payments.infrastructure.adapters.output.persistence.JpaPaymentRepositoryAdapter;

@RestController
@RequestMapping("/health")
public class HealthController {
    private JpaPaymentRepositoryAdapter repository;

    @GetMapping
    public String health(){
        return "Ok";
    }

    @PostMapping("/test-payment")
    
    public Payment createTest() {
        Payment payment = Payment.builder()
                .amount(new BigDecimal("100"))
                .currency("USD")
                .status(PaymentStatus.PENDING)
                .idempotencyKey("test-key")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return repository.save(payment);
}
}
