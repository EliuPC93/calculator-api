package com.calculator.controller;

import com.calculator.core.service.UserService;
import com.calculator.data.request.NewUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

public class UserControllerIntegrationTest extends BaseSpringBootIntegrationTest {

    @Autowired
    private UserController rentalController;

    @MockBean
    private UserService userService;

    @Test
    public void createNewOperation_should_returnHttpStatusOk_when_thereAreNoErrors() throws Exception {
        NewUser expectedRequest = NewUser.builder().username("jorge@mail.com").password("heyou").build();

        UserService userService1 = mock(UserService.class);
        doNothing().when(userService1).register(expectedRequest);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(serverUri, expectedRequest, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
