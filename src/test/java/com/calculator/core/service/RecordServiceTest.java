package com.calculator.core.service;

import com.calculator.core.repository.RecordRepository;
import com.calculator.core.repository.UserRepository;
import com.calculator.core.security.CalculatorAuthenticationProvider;
import com.calculator.data.entity.Credit;
import com.calculator.data.entity.Operation;
import com.calculator.data.entity.Record;
import com.calculator.data.entity.User;
import com.calculator.data.response.RecordDto;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTest {
    @Mock
    private RecordRepository recordRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CalculatorAuthenticationProvider authenticationProvider;
    @InjectMocks
    RecordService recordService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void registerRecord_should_createFirstRecord() {
        Set<Record> records = new CopyOnWriteArraySet<>();
        User user = User.builder().username("a name").password("a password").records(records).status(true).build();
        Credit credit = Credit.builder().user(user).amount(100.0).build();
        List<Credit> credits = new ArrayList<>();
        credits.add(credit);
        user.setCredits(credits);
        Operation operation = Operation.builder().type("addition").cost(23.0).build();
        String expectedOperationResponse = "123";
        String expectedCorrelationId = "correlationId";

        recordService.register(user, operation, expectedOperationResponse, expectedCorrelationId);

        verify(recordRepository).save(any(Record.class));
    }

    @Test
    public void registerRecord_should_createSecondRecord() {
        Set<Record> records = new CopyOnWriteArraySet<>();
        Record firstRecord = Record.builder().userBalance(90.0).amount(10.0).date(LocalDateTime.now()).active(true).build();
        records.add(firstRecord);
        User user = User.builder().username("a name").password("a password").records(records).status(true).build();
        List<Credit> credits = new ArrayList<>();
        Credit credit = Credit.builder().user(user).amount(100.0).build();
        credits.add(credit);
        user.setCredits(credits);
        Operation operation = Operation.builder().type("addition").cost(23.0).build();
        String expectedOperationResponse = "123";
        String expectedCorrelationId = "correlationId";

        recordService.register(user, operation, expectedOperationResponse, expectedCorrelationId);

        verify(recordRepository).save(any(Record.class));
    }

    @Test
    public void registerRecord_should_throw_whenInsufficientCredit() {
        Set<Record> records = new CopyOnWriteArraySet<>();
        Record firstRecord = Record.builder().userBalance(0.0).date(LocalDateTime.now()).amount(50.0).active(true).build();
        records.add(firstRecord);
        User user = User.builder().username("a name").password("a password").records(records).status(true).build();
        List<Credit> credits = new ArrayList<>();
        Credit credit = Credit.builder().user(user).amount(30.0).build();
        credits.add(credit);
        user.setCredits(credits);
        Operation operation = Operation.builder().type("addition").cost(23.0).build();
        String expectedOperationResponse = "123";
        String expectedCorrelationId = "correlationId";

        expectedException.expectMessage("Insufficient credit");
        recordService.register(user, operation, expectedOperationResponse, expectedCorrelationId);
    }

    @Test
    public void delete_should_throwException_whenUserIsNotFound() {
        String expectedUserId = "123";
        when(authenticationProvider.getUserId()).thenReturn(expectedUserId);
        when(userRepository.findById(expectedUserId)).thenReturn(Optional.empty());

        expectedException.expectMessage("User not found");
        recordService.delete("123");
    }

    @Test
    public void delete_should_throwException_whenRecordIsNotFound() {
        String expectedUserId = "123";
        String expectedRecordId = "456";
        when(authenticationProvider.getUserId()).thenReturn(expectedUserId);
        Optional<User> optionalUser = Optional.of(User.builder().username("a name").password("a password").status(true).build());
        when(userRepository.findById(expectedUserId)).thenReturn(optionalUser);
        when(recordRepository.findById(expectedRecordId)).thenReturn(Optional.empty());

        expectedException.expectMessage("Record not found");
        recordService.delete(expectedRecordId);
    }

    @Test
    public void delete_should_setStatusFalseToExistingRecord() {
        String expectedUserId = "123";
        String expectedRecordId = "456";
        when(authenticationProvider.getUserId()).thenReturn(expectedUserId);
        Optional<User> optionalUser = Optional.of(User.builder().username("a name").password("a password").status(true).build());
        when(userRepository.findById(expectedUserId)).thenReturn(optionalUser);
        Optional<Record> existingRecord = Optional.of(Record.builder().userBalance(90.0).date(LocalDateTime.now()).active(true).build());
        when(recordRepository.findById(expectedRecordId)).thenReturn(existingRecord);

        recordService.delete(expectedRecordId);

        Record updatedRecord = existingRecord.get();
        updatedRecord.setActive(false);
        verify(recordRepository).save(updatedRecord);
    }

    @Test
    public void fetchRecords_should_throwException_whenUserIsNotFound() {
        String expectedUserId = "123";
        when(authenticationProvider.getUserId()).thenReturn(expectedUserId);
        when(userRepository.findById(expectedUserId)).thenReturn(Optional.empty());

        expectedException.expectMessage("User not found");
        recordService.fetchRecords(0);
    }
    @Test
    public void fetchRecords_should_returnUserActiveRecords() {
        String expectedUserId = "123";
        Integer expectedPage = 0;
        when(authenticationProvider.getUserId()).thenReturn(expectedUserId);
        Optional<User> optionalUser = Optional.of(User.builder().username("a name").password("a password").status(true).build());
        when(userRepository.findById(expectedUserId)).thenReturn(optionalUser);
        List<Record> records = new ArrayList<>();
        Operation existingOperation = Operation.builder().type("addition").cost(23.0).build();
        Record firstRecord = Record.builder().userBalance(10.0).date(LocalDateTime.now()).operation(existingOperation).active(true).build();
        Record secondRecord = Record.builder().userBalance(20.0).date(LocalDateTime.now()).operation(existingOperation).active(false).build();
        records.add(firstRecord);
        records.add(secondRecord);
        Pageable expectedPageWithElements = PageRequest.of(expectedPage, 5);

        when(recordRepository.findByUserId(expectedUserId, expectedPageWithElements)).thenReturn(records);
        List<RecordDto> result = recordService.fetchRecords(expectedPage);

        Assert.assertEquals(result.size(), 1);
    }

}
