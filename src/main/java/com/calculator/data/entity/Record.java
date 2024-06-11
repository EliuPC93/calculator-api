package com.calculator.data.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Record")
@Table(name = "record")
@Builder
public class Record extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "operation_response")
    private Boolean operationResponse;

    @Column(name = "user_balance")
    private Double userBalance;

    @Column(name = "date")
    private LocalDateTime date;

}
