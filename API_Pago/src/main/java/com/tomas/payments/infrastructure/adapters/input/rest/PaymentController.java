package com.tomas.payments.infrastructure.adapters.input.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.tomas.payments.application.ports.input.CreatePaymentUseCase;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentRequest;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentResponse;
import com.tomas.payments.infrastructure.adapters.input.rest.mapper.PaymentRestMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final CreatePaymentUseCase createPayment;

    public PaymentController(CreatePaymentUseCase createPayment) {
        this.createPayment = createPayment;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@Valid @RequestBody PaymentRequest request) {
        var payment = PaymentRestMapper.toDomain(request);
        var savedPayment = createPayment.createPayment(payment);
        return PaymentRestMapper.toResponse(savedPayment);
    }
}
