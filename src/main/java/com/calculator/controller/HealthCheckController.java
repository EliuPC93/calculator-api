package com.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.calculator.core.exception.CalculatorException;
import com.calculator.data.request.NewUser;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class HealthCheckController {


    @Operation(method = "GET", operationId = "healthCheck", summary = "Base route for health check",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
            })
    @GetMapping
    public String healthCheck() throws CalculatorException {
        return "OK";
    }
}
