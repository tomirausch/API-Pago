package com.tomas.payments.infrastructure.adapters.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.tomas.payments.infrastructure.adapters.input.rest.exceptions.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private LocalDateTime timestamp;
    private ErrorCode code;
    private List<String> errors;
    private String path;

}
