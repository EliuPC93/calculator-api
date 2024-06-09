package com.ipalma.calculator.core.exception;

public enum ErrorCode {

    DATABASE_ERROR("COM-001"),
    VALIDATION_ERROR("COM-002"),
    INTERNAL_SERVER_ERROR("COM-003");

    String errorCode;

    ErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return this.errorCode;
    }
}
