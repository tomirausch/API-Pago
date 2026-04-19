package com.tomas.payments.infrastructure.adapters.input.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomas.payments.application.dto.PaymentRequest;
import com.tomas.payments.application.dto.PaymentResponse;
import com.tomas.payments.application.services.CreatePayment;
import com.tomas.payments.domain.model.Payment;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private CreatePayment createPayment;

    @PostMapping
    public PaymentResponse createPayment(@RequestBody PaymentRequest request) {
        var payment = Payment.builder()
            .idempotencyKey(request.getIdempotencyKey())
            .amount(request.getAmount())
            .currency(request.getCurrency())
            .build();
        createPayment.createPayment(payment);
        return PaymentResponse.builder()
            .idempotencyKey(payment.getIdempotencyKey())
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .build();
    }
    
}
