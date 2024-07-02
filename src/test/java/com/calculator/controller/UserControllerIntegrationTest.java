package com.calculator.controller;

import com.calculator.core.service.UserService;
import com.calculator.data.request.NewUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserControllerIntegrationTest extends BaseSpringBootIntegrationTest {

    @Autowired
    private UserController rentalController;

    @MockBean
    private UserService userService;

    @Test
    public void register_should_returnHttpStatusOk_when_thereAreNoErrors() throws Exception {
        NewUser expectedRequest = NewUser.builder().username("jorge@mail.com").password("heyou").build();

        doNothing().when(userService).register(expectedRequest);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(serverUri+"/users", expectedRequest, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void register_should_returnHttpStatusBadRequest_when_usernameIsMissing() throws Exception {
        NewUser expectedRequest = NewUser.builder().password("heyou").build();

        doNothing().when(userService).register(expectedRequest);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(serverUri+"/users", expectedRequest, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    public void register_should_returnHttpStatusBadRequest_when_passwordIsMissing() throws Exception {
        NewUser expectedRequest = NewUser.builder().username("pedro@mail.com").build();

        doNothing().when(userService).register(expectedRequest);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(serverUri+"/users", expectedRequest, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
