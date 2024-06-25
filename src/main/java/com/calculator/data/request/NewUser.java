package com.calculator.data.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewUser {

    @NotEmpty(message = "Invalid username")
    @Size(max = 64, min = 10,message = "Invalid username length, range value [10-64]")
    private String username;

    @NotEmpty(message = "Invalid password length")
    @Size(max = 128, min = 5, message = "Invalid password length, range value [5-128]")
    private String password;

}
