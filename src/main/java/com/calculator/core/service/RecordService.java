package com.calculator.core.service;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.exception.ErrorCode;
import com.calculator.core.repository.RecordRepository;
import com.calculator.core.repository.UserRepository;
import com.calculator.core.security.CalculatorAuthenticationProvider;
import com.calculator.data.entity.Operation;
import com.calculator.data.entity.Record;
import com.calculator.data.entity.User;
import com.calculator.data.response.RecordDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalculatorAuthenticationProvider authenticationProvider;


    public void register(User user, Operation operation, String operationResponse, String correlationId) {
        Double balance = user.getCredits().get(0).getAmount();

        List<Record> records =  user.getRecords().stream().filter(Record::getActive).collect(Collectors.toList());
        if (!records.isEmpty()) {
            records.sort(Comparator.comparing(Record::getDate).reversed());
            balance = records.get(0).getUserBalance();
        }

        if (balance < operation.getCost()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Insufficient credit");
        }

        Record record = Record.builder()
                .user(user)
                .operation(operation)
                .active(true)
                .date(LocalDateTime.now())
                .amount(operation.getCost())
                .operationResponse(operationResponse)
                .userBalance(balance - operation.getCost())
                .build();

        recordRepository.save(record);
        log.debug("{} - New record have been stored with id {}", correlationId, record.getId());
    }

    public void delete(String id) throws CalculatorException {
        String correlationId = UUID.randomUUID().toString();

        String userId = authenticationProvider.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "User not found");
        }

        Optional<Record> record = recordRepository.findById(id);

        if (record.isPresent()) {
            Record updatedRecord = record.get();
            updatedRecord.setActive(false);
            recordRepository.save(updatedRecord);
            log.debug("{} - Record have been deleted with id {}", correlationId, updatedRecord.getId());
        } else {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Record not found");
        }
    }

    public List<RecordDto> fetchRecords(Integer page) {
        String userId = authenticationProvider.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "User not found");
        }
        Pageable pageWithElements = PageRequest.of(page, 5);

        List<Record> records = recordRepository.findByUserId(userId, pageWithElements);

        return records.stream().filter(Record::getActive).map(RecordDto::from).collect(Collectors.toList());
    }
}
