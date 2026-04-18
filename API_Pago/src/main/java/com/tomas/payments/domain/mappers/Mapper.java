package com.tomas.payments.domain.mappers;

import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.infraestructure.persistence.PaymentEntity;

public class Mapper {

    public static Payment toDomain(PaymentEntity entity){
        return Payment.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .idempotencyKey(entity.getIdempotencyKey())
                .status(entity.getStatus())
                .build();
    }

    public static PaymentEntity toEntity(Payment payment){
        return PaymentEntity.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .idempotencyKey(payment.getIdempotencyKey())
                .status(payment.getStatus())
                .build();
    }

}
