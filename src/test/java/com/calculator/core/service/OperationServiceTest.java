package com.calculator.core.service;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.repository.OperationRepository;
import com.calculator.core.repository.UserRepository;
import com.calculator.core.security.CalculatorAuthenticationProvider;
import com.calculator.data.entity.Operation;
import com.calculator.data.entity.User;
import com.calculator.data.request.NewOperation;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {
    @Mock
    private RecordService recordService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OperationRepository operationRepository;
    @Mock
    private RandomStringService randomStringService;
    @Mock
    private CalculatorAuthenticationProvider authenticationProvider;

    @InjectMocks
    OperationService operationService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void registerOperation_should_throwException_whenUserIsNotFound() throws CalculatorException {
        String expectedUserId = "123";
        when(authenticationProvider.getUserId()).thenReturn(expectedUserId);
        when(userRepository.findById(expectedUserId)).thenReturn(Optional.empty());
        NewOperation expectedRequest = NewOperation.builder().type("addition").number1(2.1).number2(2.2).build();

        expectedException.expectMessage("User not found");
        operationService.registerOperation(expectedRequest);
    }

    @Test
    public void registerOperation_should_saveNewOperation_and_returnExpectedOperationResponse() throws CalculatorException {
        String expectedUserId = "123";
        when(authenticationProvider.getUserId()).thenReturn(expectedUserId);
        Optional<User> optionalUser = Optional.of(User.builder().username("a name").password("a password").status(true).build());
        when(userRepository.findById(expectedUserId)).thenReturn(optionalUser);
        NewOperation expectedRequest = NewOperation.builder().type("addition").number1(2.1).number2(2.2).build();
        when(operationRepository.findByType(expectedRequest.getType())).thenReturn(Optional.empty());
        String expectedOperationResponse = Double.toString(2.1 + 2.2);

        String response = operationService.registerOperation(expectedRequest);

        verify(operationRepository).save(any(Operation.class));
        verify(recordService).register(any(User.class), any(Operation.class), any(String.class), any(String.class));

        Assert.assertEquals(response, expectedOperationResponse);
    }
    @Test
    public void registerOperation_should_useExistingOperation_and_returnExpectedOperationResponse() throws CalculatorException {
        String expectedUserId = "123";
        when(authenticationProvider.getUserId()).thenReturn(expectedUserId);
        Optional<User> optionalUser = Optional.of(User.builder().username("a name").password("a password").status(true).build());
        when(userRepository.findById(expectedUserId)).thenReturn(optionalUser);
        NewOperation expectedRequest = NewOperation.builder().type("addition").number1(2.1).number2(2.2).build();
        Optional<Operation> existingOperation = Optional.of(Operation.builder().type(expectedRequest.getType()).cost(23.0).build());
        when(operationRepository.findByType(expectedRequest.getType())).thenReturn(existingOperation);
        String expectedOperationResponse = Double.toString(2.1 + 2.2);

        String response = operationService.registerOperation(expectedRequest);

        verify(operationRepository, times(0)).save(any(Operation.class));
        verify(recordService).register(any(User.class), any(Operation.class), any(String.class), any(String.class));

        Assert.assertEquals(response, expectedOperationResponse);
    }
}
