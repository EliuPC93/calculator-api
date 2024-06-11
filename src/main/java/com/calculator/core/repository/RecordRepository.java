package com.calculator.core.repository;

import com.calculator.data.entity.Record;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends CrudRepository<Record, String> {
    @Query(value = "select * from record where user_id = ?1 ORDER BY date DESC", nativeQuery = true)
    public List<Record> findByUserIdSortedByDate(String userId);
}
