package com.calculator.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Operation")
@Table(name = "operation")
@Builder
public class Operation extends BaseEntity {

    @Column(name = "type")
    private OperationType type;

    @Column(name = "cost")
    Double cost;
}
