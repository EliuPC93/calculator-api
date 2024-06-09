package com.ipalma.calculator.core.service;

import com.ipalma.calculator.core.exception.ErrorCode;
import com.ipalma.calculator.core.exception.UserException;
import com.ipalma.calculator.core.repository.UserRepository;
import com.ipalma.calculator.data.entity.User;
import com.ipalma.calculator.data.request.NewUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncryptor;

    public void register(NewUser newUser) {
        String correlationId = UUID.randomUUID().toString();

        Optional<User> optionalUserByEmail = userRepository.findByEmail(newUser.getEmail());
        if (optionalUserByEmail.isPresent()) {
            throw new UserException(ErrorCode.VALIDATION_ERROR, "Email already exists");
        }

        log.debug("{} - Encrypting password for new user");
        String encryptedPassword = passwordEncryptor.encode(newUser.getPassword());

        log.debug("{} - Creating authentication details for user");

        User user = new User(newUser.getEmail(), encryptedPassword, true);
        userRepository.save(user);
        log.debug("{} - New user have been stored with id {}", correlationId, user.getId());
    }
}
