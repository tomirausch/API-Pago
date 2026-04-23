package com.tomas.payments.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomas.payments.domain.model.PaymentStatus;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.PaymentRequest;
import com.tomas.payments.infrastructure.adapters.input.rest.dto.UpdateStatusRequest;
import com.tomas.payments.infrastructure.adapters.output.persistence.SpringDataPaymentRepository;
import com.tomas.payments.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PaymentControllerIntegrationTest {

    private static final String API_BASE_PATH = "/api/v1/payments";
    private static final String TEST_USERNAME = "testuser";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringDataPaymentRepository paymentRepository;

    @Autowired
    private JwtService jwtService;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        // Generate JWT token for testing
        jwtToken = jwtService.generateToken(TEST_USERNAME);
    }

    private String bearerToken() {
        return "Bearer " + jwtToken;
    }

    @Test
    @DisplayName("Should create payment successfully")
    void shouldCreatePaymentSuccessfully() throws Exception {
        PaymentRequest request = new PaymentRequest("test-key-1", new BigDecimal("100.00"), "USD");

        mockMvc.perform(post(API_BASE_PATH)
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idempotencyKey").value("test-key-1"))
            .andExpect(jsonPath("$.amount").value(100.00))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andDo(print());
    }

    @Test
    @DisplayName("Should return OK when creating payment with existing idempotency key")
    void shouldReturnOkWhenCreatingPaymentWithExistingIdempotencyKey() throws Exception {
        PaymentRequest request = new PaymentRequest("duplicate-key", new BigDecimal("50.00"), "EUR");

        mockMvc.perform(post(API_BASE_PATH)
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        mockMvc.perform(post(API_BASE_PATH)
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 when request has invalid data")
    void shouldReturn400WhenRequestHasInvalidData() throws Exception {
        PaymentRequest request = new PaymentRequest("", BigDecimal.ZERO, "");

        mockMvc.perform(post(API_BASE_PATH)
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should find payment by idempotency key")
    void shouldFindPaymentByIdempotencyKey() throws Exception {
        PaymentRequest request = new PaymentRequest("search-key", new BigDecimal("75.50"), "USD");

        mockMvc.perform(post(API_BASE_PATH)
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        mockMvc.perform(get(API_BASE_PATH + "/idempotency/search-key")
                .header("Authorization", bearerToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idempotencyKey").value("search-key"));
    }

    @Test
    @DisplayName("Should return 404 when payment not found by idempotency key")
    void shouldReturn404WhenPaymentNotFoundByIdempotencyKey() throws Exception {
        mockMvc.perform(get(API_BASE_PATH + "/idempotency/non-existent-key")
                .header("Authorization", bearerToken()))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update payment status successfully")
    void shouldUpdatePaymentStatusSuccessfully() throws Exception {
        PaymentRequest createRequest = new PaymentRequest("update-status-key", new BigDecimal("200.00"), "USD");
        String createResponse = mockMvc.perform(post(API_BASE_PATH)
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        UUID paymentId = UUID.fromString(objectMapper.readTree(createResponse).get("id").asText().replaceAll("\"", ""));

        UpdateStatusRequest updateRequest = new UpdateStatusRequest(PaymentStatus.COMPLETED);
        mockMvc.perform(patch(API_BASE_PATH + "/" + paymentId + "/status")
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent payment status")
    void shouldReturn404WhenUpdatingNonExistentPaymentStatus() throws Exception {
        UpdateStatusRequest updateRequest = new UpdateStatusRequest(PaymentStatus.COMPLETED);

        mockMvc.perform(patch(API_BASE_PATH + "/" + UUID.randomUUID() + "/status")
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when updating to invalid status")
    void shouldReturn400WhenUpdatingToInvalidStatus() throws Exception {
        PaymentRequest createRequest = new PaymentRequest("invalid-transition-key", new BigDecimal("100.00"), "USD");
        String createResponse = mockMvc.perform(post(API_BASE_PATH)
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        UUID paymentId = UUID.fromString(objectMapper.readTree(createResponse).get("id").asText().replaceAll("\"", ""));

        UpdateStatusRequest completeRequest = new UpdateStatusRequest(PaymentStatus.COMPLETED);
        mockMvc.perform(patch(API_BASE_PATH + "/" + paymentId + "/status")
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(completeRequest)))
            .andExpect(status().isOk());

        UpdateStatusRequest failRequest = new UpdateStatusRequest(PaymentStatus.FAILED);
        mockMvc.perform(patch(API_BASE_PATH + "/" + paymentId + "/status")
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should find payment by ID")
    void shouldFindPaymentById() throws Exception {
        PaymentRequest createRequest = new PaymentRequest("find-by-id-key", new BigDecimal("150.00"), "EUR");
        String createResponse = mockMvc.perform(post(API_BASE_PATH)
                .header("Authorization", bearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        UUID paymentId = UUID.fromString(objectMapper.readTree(createResponse).get("id").asText().replaceAll("\"", ""));

        mockMvc.perform(get(API_BASE_PATH + "/" + paymentId)
                .header("Authorization", bearerToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(paymentId.toString()))
            .andExpect(jsonPath("$.idempotencyKey").value("find-by-id-key"));
    }

    @Test
    @DisplayName("Should return 404 when payment not found by ID")
    void shouldReturn404WhenPaymentNotFoundById() throws Exception {
        mockMvc.perform(get(API_BASE_PATH + "/" + UUID.randomUUID())
                .header("Authorization", bearerToken()))
            .andExpect(status().isNotFound());
    }
}
