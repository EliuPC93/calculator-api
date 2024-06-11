package com.calculator.core.repository;

import com.calculator.data.entity.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


@Repository
public interface RecordRepository extends PagingAndSortingRepository<Record, String> {
    List<Record> findByUserId(String userId, Pageable pageable);
}
