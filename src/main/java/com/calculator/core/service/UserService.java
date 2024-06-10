package com.calculator.core.service;

import com.calculator.core.repository.CreditRepository;
import com.calculator.data.entity.Credit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.calculator.core.exception.ErrorCode;
import com.calculator.core.exception.CalculatorException;
import com.calculator.core.repository.UserRepository;
import com.calculator.data.entity.AuthenticationDetail;
import com.calculator.data.entity.SecurityRole;
import com.calculator.data.entity.User;
import com.calculator.data.request.NewUser;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private CreditRepository creditRepository;
    private PasswordEncoder passwordEncryptor;

    public void register(NewUser newUser) {
        String correlationId = UUID.randomUUID().toString();

        Optional<User> optionalUserByUsername = userRepository.findByUsername(newUser.getUsername());
        if (optionalUserByUsername.isPresent()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Username already exists");
        }

        log.debug("{} - Encrypting password for new user");
        String encryptedPassword = passwordEncryptor.encode(newUser.getPassword());

        log.debug("{} - Creating authentication details for user");
        AuthenticationDetail authenticationDetail = AuthenticationDetail.builder()
                .roles(SecurityRole.ADMIN.getRole()).build();

        User user = User.builder()
                .username(newUser.getUsername())
                .password(encryptedPassword)
                .authenticationDetail(authenticationDetail)
                .build();

        userRepository.save(user);

        log.debug("{} - New user have been stored with id {}", correlationId, user.getId());

        Credit firstCredit = Credit.builder()
                .amount(1000.00)
                .user(user)
                .build();

        creditRepository.save(firstCredit);

        log.debug("{} - New credit {} have been created to user with id {}", correlationId, firstCredit.getId(), user.getId());
    }
}
