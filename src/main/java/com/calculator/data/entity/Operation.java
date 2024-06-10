package com.calculator.data.entity;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Operation")
@Builder
public class Operation extends BaseEntity {
    OperationType type;
    Double cost;
}
