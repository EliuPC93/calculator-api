package com.calculator.data.entity;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public enum OperationType {

    ADDITION("addition"),
    SUBTRACTION("subtraction"),
    DIVISION("division"),
    MULTIPLICATION("multiplication"),
    SQUAREROOT("square_root"),
    RANDOMSTRING("random_string");
    String value;

    OperationType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    public static OperationType fromValue(String value) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.value.equals(value)){
                return operationType;
            }
        }
        throw new InvalidParameterException("Unknown operation of: " + value);
    }

    public Double getCost() {
        Map<String, Double> hm = new HashMap<String, Double>();

        hm.put(OperationType.ADDITION.value, 10.00);
        hm.put(OperationType.SUBTRACTION.value, 20.00);
        hm.put(OperationType.DIVISION.value, 30.00);
        hm.put(OperationType.MULTIPLICATION.value, 40.00);
        hm.put(OperationType.SQUAREROOT.value, 50.00);
        hm.put(OperationType.RANDOMSTRING.value, 60.00);

        return hm.get(this.value);
    }
}
