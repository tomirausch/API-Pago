package com.tomas.payments.infrastructure.adapters.input.rest;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomas.payments.application.ports.input.CreatePaymentUseCase;
import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentResponse;
import com.tomas.payments.infrastructure.adapters.input.rest.mapper.PaymentRestMapper;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final CreatePaymentUseCase createPaymentUseCase;

    public HealthController(CreatePaymentUseCase createPaymentUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @GetMapping
    public String health() {
        return "Ok";
    }

    @PostMapping("/test-payment")
    public PaymentResponse createTest() {
        Payment payment = Payment.create(
            "test-key-" + System.currentTimeMillis(),  // único por llamada
            new BigDecimal("100"),
            "USD"
        );
        Payment saved = createPaymentUseCase.createPayment(payment);
        return PaymentRestMapper.toResponse(saved);
    }
}