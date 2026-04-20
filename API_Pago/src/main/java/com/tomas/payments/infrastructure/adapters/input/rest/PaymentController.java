package com.tomas.payments.infrastructure.adapters.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import com.tomas.payments.application.ports.input.CreatePaymentUseCase;
import com.tomas.payments.application.ports.input.GetPaymentUseCase;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentRequest;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentResponse;
import com.tomas.payments.infrastructure.adapters.input.rest.mapper.PaymentRestMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final CreatePaymentUseCase createPayment;
    private final GetPaymentUseCase getPaymentUseCase;

    public PaymentController(CreatePaymentUseCase createPayment, GetPaymentUseCase getPaymentUseCase) {
        this.createPayment = createPayment;
        this.getPaymentUseCase = getPaymentUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@Valid @RequestBody PaymentRequest request) {
        var payment = PaymentRestMapper.toDomain(request);
        var savedPayment = createPayment.createPayment(payment);
        return PaymentRestMapper.toResponse(savedPayment);
    }
    
    @GetMapping("/idempotency/{key}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable @NotBlank String key) {
        return getPaymentUseCase.findByIdempotencyKey(key)
            .map(payment -> ResponseEntity.ok(PaymentRestMapper.toResponse(payment)))
            .orElse(ResponseEntity.notFound().build());
    }
}
