package com.tomas.payments.infrastructure.adapters.output.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.domain.ports.output.PaymentRepository;

@Repository
public class PaymentJpa implements PaymentRepository {

    private final SpringDataPaymentsRepository repository;

    public PaymentJpa(SpringDataPaymentsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey).map(Mapper::toDomain);
    }

    @Override
    public Payment save(Payment payment) {
        return Mapper.toDomain(repository.save(Mapper.toEntity(payment)));
    }
}
