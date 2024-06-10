package com.calculator.core.repository;

import com.calculator.data.entity.Credit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CreditRepository extends CrudRepository<Credit, String> {
}
