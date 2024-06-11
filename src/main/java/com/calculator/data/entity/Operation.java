package com.calculator.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Operation")
@Table(name = "operation")
@Builder
public class Operation extends BaseEntity {

    @Column(name = "type")
    private String type;

    @Column(name = "cost")
    Double cost;
}
