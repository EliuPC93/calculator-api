package com.calculator.data.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum SecurityRole {

    ADMIN("ADMIN");

    String role;
    private static final String DELIMITER = ",";

    SecurityRole(String role) {
        this.role = role;
    }

    public static List<String> getRoles(String roles) {
        return Arrays.asList(roles.split(DELIMITER));
    }
}
