package com.calculator.controller;

import com.calculator.data.request.NewCredit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @Operation(method = "PUT", operationId = "addCredit", summary = "Add credit to a user.",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Bad request", responseCode = "400")
            })
    @PutMapping("/credit")
    public void extendCredit(@Valid @RequestBody NewCredit newCredit) throws Exception {
        try {
            userService.extendCredit(newCredit);
        } catch (CalculatorException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @MessageMapping("/balance")
    @SendTo("/user/balance")
    public Double getUserBalance(String username) throws Exception {
        try {
            Double balance = userService.retrieveUserBalance(username);
            return balance;
        } catch (CalculatorException exception) {
            throw new Exception(exception.getMessage());
        }
    }
}
