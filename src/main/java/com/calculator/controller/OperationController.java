package com.calculator.controller;

import com.calculator.core.service.OperationService;
import com.calculator.data.request.NewOperation;
import com.calculator.data.response.OperationResponseDto;
import com.calculator.data.response.RecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.calculator.core.exception.CalculatorException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

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

    @Operation(method = "GET", operationId = "fetchOperations", summary = "Get operations for user.",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RecordDto.class))
                    })
            }, parameters = {
            @Parameter(name = "page", description = "Page number"),
    })
    @GetMapping
    public List<RecordDto> fetchOperations(@RequestParam("page") Integer page) throws CalculatorException {
        return operationService.fetchOperations(page);
    }

    @Operation(method = "DELETE", operationId = "deleteOperation", summary = "Deletes an operation.",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Bad request", responseCode = "400")
            })
    @DeleteMapping("{id}")
    public void deleteOperation(@PathVariable("id") @NotEmpty(message = "Record id should not be null or empty")  String id) throws CalculatorException {
        operationService.deleteRecord(id);
    }
}
