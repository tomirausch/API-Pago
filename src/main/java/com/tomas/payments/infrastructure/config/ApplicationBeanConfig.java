package com.tomas.payments.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tomas.payments.application.ports.input.CreatePaymentUseCase;
import com.tomas.payments.application.ports.input.GetPaymentUseCase;
import com.tomas.payments.application.ports.input.UpdatePaymentStatusUseCase;
import com.tomas.payments.application.ports.output.PaymentRepositoryPort;
import com.tomas.payments.application.services.CreatePayment;
import com.tomas.payments.application.services.GetPayment;
import com.tomas.payments.application.services.UpdateStatusPayment;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public CreatePaymentUseCase createPaymentUseCase(PaymentRepositoryPort paymentRepositoryPort) {
        return new CreatePayment(paymentRepositoryPort);
    }

    @Bean
    public GetPaymentUseCase getPaymentUseCase(PaymentRepositoryPort paymentRepositoryPort) {
        return new GetPayment(paymentRepositoryPort);
    }

    @Bean
    public UpdatePaymentStatusUseCase updatePaymentStatusUseCase(PaymentRepositoryPort paymentRepositoryPort) {
        return new UpdateStatusPayment(paymentRepositoryPort);
    }
}
