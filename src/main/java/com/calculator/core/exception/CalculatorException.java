package com.calculator.core.exception;

import lombok.Getter;

@Getter
public class CalculatorException extends RuntimeException {

    private ErrorCode errorCode;
    private static final String ERROR_MESSAGE =  "An error has occurred while processing request, try again";

    public CalculatorException(ErrorCode errorCode) {
        super(ERROR_MESSAGE);
        this.errorCode = errorCode;
    }

    public CalculatorException(ErrorCode errorCode, Throwable throwable) {
        super(ERROR_MESSAGE, throwable);
        this.errorCode = errorCode;
    }

    public CalculatorException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
