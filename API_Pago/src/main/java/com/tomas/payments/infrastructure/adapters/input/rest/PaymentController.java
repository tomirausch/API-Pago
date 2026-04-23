package com.tomas.payments.infrastructure.adapters.input.rest;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import com.tomas.payments.application.ports.input.CreatePaymentUseCase;
import com.tomas.payments.application.ports.input.GetPaymentUseCase;
import com.tomas.payments.application.ports.input.UpdatePaymentStatusUseCase;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentRequest;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentResponse;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.UpdateStatusRequest;
import com.tomas.payments.infrastructure.adapters.input.rest.mapper.PaymentRestMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final CreatePaymentUseCase createPayment;
    private final GetPaymentUseCase getPaymentUseCase;
    private final UpdatePaymentStatusUseCase updatePaymentStatus;

    public PaymentController(CreatePaymentUseCase createPayment, GetPaymentUseCase getPaymentUseCase, UpdatePaymentStatusUseCase updatePaymentStatus) {
        this.createPayment = createPayment;
        this.getPaymentUseCase = getPaymentUseCase;
        this.updatePaymentStatus = updatePaymentStatus;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        var payment = PaymentRestMapper.toDomain(request);
        var savedPayment = createPayment.createPayment(payment);
        var response = PaymentRestMapper.toResponse(savedPayment.getPayment());
        return ResponseEntity
                .status(savedPayment.isNew() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/idempotency/{key}")
    public ResponseEntity<PaymentResponse> getByIdempotencyKey(@PathVariable @NotBlank String key) {
        return getPaymentUseCase.findByIdempotencyKey(key)
            .map(payment -> ResponseEntity.ok(PaymentRestMapper.toResponse(payment)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStatusRequest request) {
        var updatedPayment = updatePaymentStatus.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(PaymentRestMapper.toResponse(updatedPayment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable UUID id) {
        return getPaymentUseCase.findById(id)
            .map(payment -> ResponseEntity.ok(PaymentRestMapper.toResponse(payment)))
            .orElse(ResponseEntity.notFound().build());
    }
}
