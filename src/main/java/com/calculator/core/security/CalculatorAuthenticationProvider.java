package com.calculator.core.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.calculator.core.repository.UserRepository;
import com.calculator.data.entity.User;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CalculatorAuthenticationProvider {

    private UserRepository userRepository;

    public String getUserId() {
        String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> existingUser =  userRepository.findByUsername(user);
        if (existingUser.isPresent()) {
            return existingUser.get().getId();
        } else {
            return "notFound";
        }
    }

}
