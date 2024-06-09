package com.ipalma.calculator.data.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    private String id;

    protected BaseEntity() {
        id = UUID.randomUUID().toString();
    }
}
