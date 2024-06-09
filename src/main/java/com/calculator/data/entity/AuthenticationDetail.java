package com.calculator.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "AuthenticationDetail")
@Table(name = "authentication_detail")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticationDetail extends BaseEntity {

    @Column(name = "roles")
    private String roles;

}
