package com.ipalma.calculator.core.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private ErrorCode errorCode;
    private static final String ERROR_MESSAGE =  "An error has occurred while processing request, try again";

    public UserException(ErrorCode errorCode) {
        super(ERROR_MESSAGE);
        this.errorCode = errorCode;
    }

    public UserException(ErrorCode errorCode, Throwable throwable) {
        super(ERROR_MESSAGE, throwable);
        this.errorCode = errorCode;
    }

    public UserException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
