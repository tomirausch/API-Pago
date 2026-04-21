package com.tomas.payments.application.ports.input;

import java.util.UUID;

import com.tomas.payments.domain.model.Payment;
import com.tomas.payments.domain.model.PaymentStatus;

public interface UpdatePaymentStatusUseCase {
    Payment updateStatus(UUID paymentId, PaymentStatus newStatus);
}
