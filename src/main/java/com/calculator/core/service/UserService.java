package com.calculator.core.service;

import com.calculator.core.repository.CreditRepository;
import com.calculator.core.security.CalculatorAuthenticationProvider;
import com.calculator.data.entity.Credit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private PasswordEncoder passwordEncryptor;
    @Autowired
    private CalculatorAuthenticationProvider authenticationProvider;

    public void register(NewUser newUser) throws CalculatorException {
        String correlationId = UUID.randomUUID().toString();

        Optional<User> optionalUserByUsername = userRepository.findByUsername(newUser.getUsername());
        if (optionalUserByUsername.isPresent()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Username already exists");
        }

        log.debug("{} - Encrypting password for new user", correlationId);
        String encryptedPassword = passwordEncryptor.encode(newUser.getPassword());

        log.debug("{} - Creating authentication details for user", correlationId);
        AuthenticationDetail authenticationDetail = AuthenticationDetail.builder()
                .roles(SecurityRole.ACTIVE.getRole()).build();

        User user = User.builder()
                .username(newUser.getUsername())
                .password(encryptedPassword)
                .authenticationDetail(authenticationDetail)
                .build();

        userRepository.save(user);

        log.debug("{} - New user have been stored with id {}", correlationId, user.getId());

        addCredit(1000.00, user, correlationId);
    }

    public void extendCredit (Double amount) {
        String correlationId = UUID.randomUUID().toString();

        String userId = authenticationProvider.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "User not found");
        }

        if (amount <= 0) {
            throw new CalculatorException(ErrorCode.VALIDATION_ERROR, "Amount should be positive");
        }

        addCredit(amount, optionalUser.get(), correlationId);
    }

    private void addCredit (Double amount, User user, String correlationId) {
        Credit newCredit = Credit.builder().amount(amount).user(user).build();

        creditRepository.save(newCredit);

        log.debug("{} - New credit {} have been added to user with id {}", correlationId, newCredit.getId(), user.getId());
    }
}
