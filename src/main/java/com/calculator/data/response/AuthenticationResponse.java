package com.calculator.data.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AuthenticationResponse {

    private String jwt;
    private List<String> roles;

}
