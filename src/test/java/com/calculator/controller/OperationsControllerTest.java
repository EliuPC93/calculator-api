package com.calculator.controller;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.exception.ErrorCode;
import com.calculator.core.service.OperationService;
import com.calculator.data.request.NewOperation;
import com.calculator.data.response.OperationResponseDto;
import com.calculator.data.response.RecordDto;
import com.calculator.data.response.RecordsReponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class OperationsControllerTest extends BaseSpringBootIntegrationTest {

    @Autowired
    private OperationController rentalController;

    @MockBean
    private OperationService operationService;

    @Test
    public void register_should_returnHttpStatusOk_when_thereAreNoErrors() throws Exception {
        String expectedResult = "123";
        NewOperation expectedRequest = NewOperation.builder().type("addition").number1(2.1).number2(2.2).build();
        OperationResponseDto expectedResponse = OperationResponseDto.builder().result(expectedResult).build();

        when(operationService.registerOperation(expectedRequest)).thenReturn(expectedResult);

        ResponseEntity<OperationResponseDto> responseEntity = restTemplate.postForEntity(serverUri+"/operations", expectedRequest, OperationResponseDto.class);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        OperationResponseDto response = responseEntity.getBody();
        Assert.assertNotNull(response);
        Assert.assertEquals(expectedResponse.getResult(), response.getResult());
    }
    @Test
    public void register_should_returnHttpStatusInternalServerError_when_serviceThrowsException() throws Exception {
        NewOperation expectedRequest = new NewOperation("addition", 2.1, 1.2);

        when(operationService.registerOperation(expectedRequest)).thenThrow(new CalculatorException(ErrorCode.VALIDATION_ERROR, "something happened"));

        ResponseEntity<OperationResponseDto> responseEntity = restTemplate.postForEntity(serverUri+"/operations", expectedRequest, OperationResponseDto.class);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void fetchOperations_should_returnHttpStatusOk_when_thereAreNoErrors() throws Exception {
        RecordsReponse expectedResponse = buildRecordsResponse();
        when(operationService.fetchOperations(0)).thenReturn(expectedResponse.getRecords());

        ResponseEntity<RecordsReponse> responseEntity = restTemplate.getForEntity(serverUri+"/operations?page=0", RecordsReponse.class);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        RecordsReponse response = responseEntity.getBody();
        Assert.assertNotNull(response);
        Assert.assertEquals(expectedResponse.getRecords().size(), response.getRecords().size());
    }

    @Test
    public void fetchOperations_should_returnHttpStatusBadRequest_when_pageIsMissing() throws Exception {
        ResponseEntity<RecordsReponse> responseEntity = restTemplate.getForEntity(serverUri+"/operations", RecordsReponse.class);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void fetchOperations_should_returnHttpStatusInternalServerError_when_serviceThrowsException() throws Exception {
        when(operationService.fetchOperations(0)).thenThrow(new CalculatorException(ErrorCode.VALIDATION_ERROR, "something happened"));

        ResponseEntity<RecordsReponse> responseEntity = restTemplate.getForEntity(serverUri+"/operations?page=0", RecordsReponse.class);
        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
    @Test
    public void deleteOperation_should_returnHttpStatusOK_when_thereAreNoErrors() throws Exception {
        String expectedId = "123";
        Map<String, String> params = new HashMap<>();
        params.put("id", expectedId);

        doNothing().when(operationService).deleteRecord(expectedId);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(serverUri+"/operations/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, params);

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteOperation_should_returnHttpStatusMethodNotAllowed_when_idIsMissing() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", null);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(serverUri+"/operations/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, params);

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
    }
    @Test
    public void deleteOperation_should_returnHttpStatusInternalServerError_when_serviceThrows() throws Exception {
        String expectedId = "123";
        Map<String, String> params = new HashMap<>();
        params.put("id", expectedId);
        doThrow(new CalculatorException(ErrorCode.VALIDATION_ERROR, "something happened")).when(operationService).deleteRecord(expectedId);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(serverUri+"/operations/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class, params);

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    private RecordsReponse buildRecordsResponse() {
        RecordDto record1 = RecordDto.builder()
                .id("123")
                .operationType("addition")
                .date("2024-02-01")
                .amount(20.1)
                .userBalance(100.1)
                .operationResponse("400")
                .build();
        List<RecordDto> expectedRecords = new ArrayList<>();
        expectedRecords.add(record1);
        return RecordsReponse.builder().records(expectedRecords).build();
    }
}
