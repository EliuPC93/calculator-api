package com.calculator.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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
