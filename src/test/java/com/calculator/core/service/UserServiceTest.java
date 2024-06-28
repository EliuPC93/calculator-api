package com.calculator.core.service;

import com.calculator.core.exception.CalculatorException;
import com.calculator.core.repository.CreditRepository;
import com.calculator.core.repository.UserRepository;
import com.calculator.data.entity.User;
import com.calculator.data.request.NewUser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private PasswordEncoder passwordEncryptor;

    @InjectMocks
    UserService userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void register_should_throwException_whenUserIsFound() throws CalculatorException {
        NewUser expectedNewUser = NewUser.builder().username("jorge@mail.com").password("heyou").build();
        Optional<User> optionalUser = Optional.of(User.builder().username(expectedNewUser.getUsername()).password(expectedNewUser.getPassword()).status(true).build());
        when(userRepository.findByUsername(expectedNewUser.getUsername())).thenReturn(optionalUser);

        expectedException.expectMessage("Username already exists");
        userService.register(expectedNewUser);
    }

    @Test
    public void register_should_registerNewUser_whenThereAreNoErrors() throws CalculatorException {
        NewUser expectedNewUser = NewUser.builder().username("jorge@mail.com").password("heyou").build();
        when(userRepository.findByUsername(expectedNewUser.getUsername())).thenReturn( Optional.empty());
        when(passwordEncryptor.encode(expectedNewUser.getPassword())).thenReturn( "myencodedpswd");

        userService.register(expectedNewUser);

        verify(userRepository, times(1)).save(any());
        verify(creditRepository, times(1)).save(any());
    }
}
