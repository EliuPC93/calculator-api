package com.calculator.data.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewCredit {

    @NotEmpty(message = "Invalid username")
    @Size(max = 64, min = 10,message = "Invalid username length, range value [10-64]")
    private String username;

    @NotNull(message = "Invalid amount")
    @Positive(message = "Credit amount should be positive")
    private Double amount;

}
