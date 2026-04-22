package com.tomas.payments.infrastructure.adapters.input.rest.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor 
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private Instant timestamp;
    private String code;
    private List<String> errors;
    private String path;

}
