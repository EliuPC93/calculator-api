package com.calculator.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.calculator.core.exception.ErrorCode;
import com.calculator.core.exception.CalculatorException;
import com.calculator.data.response.ErrorResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CalculatorExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse calculatorExceptionHandler(CalculatorException exception) {
        if (exception.getCause() != null) {
            //some error just happened
            log.error("Error while processing request", exception.getCause());
        }
        return ErrorResponse.builder()
                .errorCode(exception.getErrorCode().toString())
                .errorMessage(exception.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorResponse> validationException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(violation ->
                        ErrorResponse.builder()
                            .errorCode(ErrorCode.VALIDATION_ERROR.toString())
                            .errorMessage(violation.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
    }

}
