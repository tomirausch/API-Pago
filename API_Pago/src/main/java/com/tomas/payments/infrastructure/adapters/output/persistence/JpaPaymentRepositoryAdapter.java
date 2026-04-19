package com.tomas.payments.infrastructure.adapters.output.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.model.Payment;

@Repository
public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {
    @Autowired
    private SpringDataPaymentRepository repository;

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey).map(Mapper::toDomain);
    }

    @Override
    public Payment save(Payment payment) {
        return Mapper.toDomain(repository.save(Mapper.toEntity(payment)));
    }
}
