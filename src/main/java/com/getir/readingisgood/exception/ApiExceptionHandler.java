package com.getir.readingisgood.exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    @Bean
    public ErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>(e.getBindingResult().getFieldErrors().size());

        e.getBindingResult().getFieldErrors().forEach(ex -> errors.add(ex.getField() + " : " + ex.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
        res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void handleMissingParams(HttpServletResponse res, MissingServletRequestParameterException ex)
        throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getParameterName() + " parameter is missing");
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong");
    }

}
