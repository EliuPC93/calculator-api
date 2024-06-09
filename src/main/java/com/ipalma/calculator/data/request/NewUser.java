package com.ipalma.calculator.data.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUser {

    @NotEmpty(message = "Invalid email")
    @Size(max = 128, min = 10, message = "Invalid email length, range value [10-128]")
    private String email;

    @NotEmpty(message = "Invalid password length")
    @Size(max = 128, min = 5, message = "Invalid password length, range value [5-128]")
    private String password;

}
