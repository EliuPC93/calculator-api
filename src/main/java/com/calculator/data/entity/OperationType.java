package com.calculator.data.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum OperationType {

    ADD("addition"),
    SUB("subtraction"),
    DIV("divisionn"),
    MUL("multiplication"),
    SQR("square_root"),
    RAN("random_string");
    String value;

    OperationType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    public static OperationType from(String value) {
        return Arrays.stream(values()).filter(g -> Objects.equals(g.value, value)).findFirst().get();
    }

    public Double getCost() {
        Map<String, Double> hm = new HashMap<String, Double>();

        hm.put(OperationType.ADD.value, 10.00);
        hm.put(OperationType.SUB.value, 20.00);
        hm.put(OperationType.DIV.value, 30.00);
        hm.put(OperationType.MUL.value, 40.00);
        hm.put(OperationType.SQR.value, 50.00);
        hm.put(OperationType.RAN.value, 60.00);

        return hm.get(this.value);
    }
}
