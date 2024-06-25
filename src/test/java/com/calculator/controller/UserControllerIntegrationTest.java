package com.calculator.controller;

import com.calculator.core.service.UserService;
import com.calculator.data.request.NewUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @Autowired
    private UserController rentalController;
    @LocalServerPort
    private int serverPort;
    @MockBean
    private UserService userService;
    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    public void createNewOperation_should_returnHttpStatusOk_when_thereAreNoErrors() throws Exception {
        NewUser expectedRequest = NewUser.builder().username("jorge@mail.com").password("heyou").build();

        UserService userService1 = mock(UserService.class);
        doNothing().when(userService1).register(expectedRequest);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + serverPort + "/v1/users", expectedRequest, String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
