package com.calculator.data.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class NewOperation {
    @NotEmpty(message = "Invalid operation")
    private String type;
}
