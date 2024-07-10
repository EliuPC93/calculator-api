package com.calculator.core.service;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.exception.ErrorCode;
import com.calculator.core.repository.OperationRepository;
import com.calculator.core.repository.UserRepository;
import com.calculator.core.security.CalculatorAuthenticationProvider;
import com.calculator.data.entity.*;
import com.calculator.data.request.NewOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class OperationService {
    @Autowired
    private RecordService recordService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private RandomStringService randomStringService;
    @Autowired
    private CalculatorAuthenticationProvider authenticationProvider;

    public String registerOperation(NewOperation newOperation) throws CalculatorException {
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

        String operationResponse = getOperationResponse(operationType, newOperation, correlationId);

        recordService.register(optionalUser.get(), operation, operationResponse, correlationId);

        return operationResponse;
    }

    private String getOperationResponse(OperationType operationType, NewOperation newOperation, String correlationId) throws IllegalStateException {
        switch (operationType) {
            case ADDITION: {
                return Double.toString(newOperation.getNumber1() + newOperation.getNumber2());
            }
            case SUBTRACTION: {
                return Double.toString(newOperation.getNumber1() - newOperation.getNumber2());
            }
            case DIVISION:
                return Double.toString(newOperation.getNumber1() / newOperation.getNumber2());
            case MULTIPLICATION:
                return Double.toString(newOperation.getNumber1() * newOperation.getNumber2());
            case SQUAREROOT:
                if (newOperation.getNumber1() < 0) {
                    throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Invalid number");
                }
                return Double.toString(Math.sqrt(newOperation.getNumber1()));
            case RANDOMSTRING:
                return randomStringService.getRandomString();
            default:
                throw new IllegalStateException("Unexpected operation: " + correlationId);
        }
    }
}
