package com.tomas.payments.infrastructure.adapters.output.persistence;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.domain.exceptions.DuplicateIdempotencyKeyException;
import com.tomas.payments.domain.model.Payment;

@Repository
public class JpaPaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final SpringDataPaymentRepository repository;

    JpaPaymentRepositoryAdapter(SpringDataPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey).map(PaymentPersistenceMapper::toDomain);
    }

    @Override
    public Payment save(Payment payment) {
        try{
            return PaymentPersistenceMapper.toDomain(repository.save(PaymentPersistenceMapper.toEntity(payment)));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateIdempotencyKeyException("Payment with idempotency key " + payment.getIdempotencyKey() + " already exists", e);
        }
    }
}
