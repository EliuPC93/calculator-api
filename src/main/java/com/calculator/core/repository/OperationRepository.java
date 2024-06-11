package com.calculator.core.repository;

import com.calculator.data.entity.Operation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationRepository extends CrudRepository<Operation, String> {
    Optional<Operation> findByType(String type);
}
