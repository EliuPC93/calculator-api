package com.calculator.controller;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.exception.ErrorCode;
import com.calculator.core.service.OperationService;
import com.calculator.data.request.NewOperation;
import com.calculator.data.response.OperationResponseDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

public class OperationsControllerTest extends BaseSpringBootIntegrationTest {

    @Autowired
    private OperationController operationController;

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
}
