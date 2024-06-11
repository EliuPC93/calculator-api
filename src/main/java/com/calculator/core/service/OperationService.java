package com.calculator.core.service;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.exception.ErrorCode;
import com.calculator.core.repository.OperationRepository;
import com.calculator.core.repository.RecordRepository;
import com.calculator.core.repository.UserRepository;
import com.calculator.core.security.CalculatorAuthenticationProvider;
import com.calculator.data.entity.*;
import com.calculator.data.entity.Record;
import com.calculator.data.request.NewOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class OperationService {
    private RecordRepository recordRepository;
    private UserRepository userRepository;
    private OperationRepository operationRepository;
    private CalculatorAuthenticationProvider authenticationProvider;

    public void register(NewOperation newOperation) {
        String correlationId = UUID.randomUUID().toString();

        String userId = authenticationProvider.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "User not found");
        }

        OperationType operationType = OperationType.fromValue(newOperation.getType());

        Operation operation = Operation.builder()
                .type(operationType.toString())
                .cost(operationType.getCost())
                .build();

        Optional<Operation> existingOperation = operationRepository.findByType(newOperation.getType());
        if (existingOperation.isPresent()) {
                operation = existingOperation.get();
        } else {
            operationRepository.save(operation);
            log.debug("{} - New operation have been stored with id {}", correlationId, operation.getId());
        }

        Double balance = optionalUser.get().getCredits().get(0).getAmount();

        List<Record> records =  optionalUser.get().getRecords();
        if (!records.isEmpty()) {
            records.sort(Comparator.comparing(Record::getDate).reversed());
            balance = records.get(0).getUserBalance();
        }

        Record record = Record.builder()
                .user(optionalUser.get())
                .operation(operation)
                .date(LocalDateTime.now())
                .amount(operation.getCost())
                .build();

        if (balance > operation.getCost()) {
            record.setOperationResponse(true);
            record.setUserBalance(balance - operation.getCost());
        } else {
            record.setOperationResponse(false);
            record.setUserBalance(balance);
        }

        recordRepository.save(record);

        log.debug("{} - New record have been stored with id {}", correlationId, record.getId());

        if (!record.getOperationResponse()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Insufficient credit");
        }
    }

}
