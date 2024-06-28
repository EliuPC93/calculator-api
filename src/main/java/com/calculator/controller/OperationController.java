package com.calculator.controller;

import com.calculator.core.service.OperationService;
import com.calculator.data.request.NewOperation;
import com.calculator.data.response.OperationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.calculator.core.exception.CalculatorException;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("operations")
@AllArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @Operation(method = "POST", operationId = "registerOperation", summary = "Registers a new operation.",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Bad request", responseCode = "400")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public OperationResponseDto register(@Valid @RequestBody NewOperation newOperation) throws Exception {
        try {
            String operationResponse = operationService.registerOperation(newOperation);
            return OperationResponseDto.builder().result(operationResponse).build();
        } catch (CalculatorException exception) {
            throw new Exception(exception.getMessage());
        }
    }
}
