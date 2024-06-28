package com.calculator.controller;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.service.RecordService;
import com.calculator.data.response.RecordDto;
import com.calculator.data.response.RecordsReponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("records")
@AllArgsConstructor
public class RecordController {
    @Autowired
    RecordService recordService;

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
    public RecordsReponse fetchOperations(@RequestParam("page") Integer page) throws Exception {
        try {
            List<RecordDto> records = recordService.fetchRecords(page);
            return RecordsReponse.builder().records(records).build();
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Operation(method = "DELETE", operationId = "deleteOperation", summary = "Deletes an operation.",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Bad request", responseCode = "400")
            })
    @DeleteMapping("{id}")
    public void deleteOperation(@PathVariable("id") @NotEmpty(message = "Record id should not be null or empty")  String id) throws Exception {
        try {
            recordService.delete(id);
        } catch (CalculatorException exception) {
            throw new Exception(exception.getMessage());
        }
    }
}
