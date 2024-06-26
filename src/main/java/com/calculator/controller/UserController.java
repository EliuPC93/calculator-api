package com.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.service.UserService;
import com.calculator.data.request.NewUser;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(method = "POST", operationId = "registerUser", summary = "Registers a new user.",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Bad request", responseCode = "400")
            })
    @PostMapping
    public void register(@Valid @RequestBody NewUser newUser) throws CalculatorException {
        userService.register(newUser);
    }
}
