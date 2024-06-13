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
import com.calculator.data.response.RecordDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class OperationService {
    private RecordRepository recordRepository;
    private UserRepository userRepository;
    private OperationRepository operationRepository;
    private CalculatorAuthenticationProvider authenticationProvider;

    public void registerOperation(NewOperation newOperation) {
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

        List<Record> records =  optionalUser.get().getRecords().stream().filter(Record::getActive).collect(Collectors.toList());
        if (!records.isEmpty()) {
            records.sort(Comparator.comparing(Record::getDate).reversed());
            balance = records.get(0).getUserBalance();
        }

        if (balance < operation.getCost()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Insufficient credit");
        }

        String operationResponse;
        switch (operationType) {
            case ADDITION: {
                operationResponse = Double.toString(newOperation.getNumber1() + newOperation.getNumber2());
                break;
            }
            case SUBTRACTION: {
                operationResponse = Double.toString(newOperation.getNumber1() - newOperation.getNumber2());
                break;
            }
            case DIVISION:
                operationResponse = Double.toString(newOperation.getNumber1() / newOperation.getNumber2());
                break;
            case MULTIPLICATION:
                operationResponse = Double.toString(newOperation.getNumber1() * newOperation.getNumber2());
                break;
            case SQUAREROOT:
                operationResponse = Double.toString(Math.sqrt(newOperation.getNumber1()));
                break;
            case RANDOMSTRING:
                // TODO: add third party library for random string
                operationResponse = "asd";
                break;
            default:
                throw new IllegalStateException("Unexpected operation: " + correlationId);
        }

        Record record = Record.builder()
                .user(optionalUser.get())
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

    public List<RecordDto> fetchOperations(Integer page) {
        String userId = authenticationProvider.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "User not found");
        }
        Pageable pageWithElements = PageRequest.of(page, 5);

        List<Record> records = recordRepository.findByUserId(userId, pageWithElements);

        return records.stream().filter(Record::getActive).map(RecordDto::from).collect(Collectors.toList());
    }

    public void deleteRecord(String id) {
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
}
