package com.ipalma.calculator.controller;

import com.ipalma.calculator.core.exception.UserException;
import com.ipalma.calculator.core.service.UserService;
import com.ipalma.calculator.data.request.NewUser;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping
    public void register(@Valid @RequestBody NewUser newUser) throws UserException {
        userService.register(newUser);
    }
}
