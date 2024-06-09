package com.ipalma.calculator.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
@Builder
public class User extends BaseEntity {

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private boolean status;

}
