package com.calculator.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Credit")
@Table(name = "credit")
@Builder
public class Credit extends BaseEntity {
    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
