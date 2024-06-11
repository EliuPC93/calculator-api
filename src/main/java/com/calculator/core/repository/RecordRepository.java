package com.calculator.core.repository;

import com.calculator.data.entity.Record;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends CrudRepository<Record, String> {
}
