package com.calculator.data.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {

    private String errorCode;
    private String errorMessage;

}
