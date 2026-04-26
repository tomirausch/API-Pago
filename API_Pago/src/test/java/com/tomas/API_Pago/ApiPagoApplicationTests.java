package com.tomas.API_Pago;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.tomas.payments.PaymentsApplication;

@SpringBootTest(classes = PaymentsApplication.class)
@ActiveProfiles("test")
class ApiPagoApplicationTests {

    @Test
    void contextLoads() {
    }

}
