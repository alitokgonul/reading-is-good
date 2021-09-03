package com.getir.readingisgood.exception;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@RequiredArgsConstructor
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
