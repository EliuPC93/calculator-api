package com.calculator.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseSpringBootIntegrationTest {
    @LocalServerPort
    private int serverPort;
    protected URI serverUri;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Before
    public void configureIntegrationEnv() throws Exception {
        serverUri = new URI("http://localhost:" + serverPort + "/v1");
    }
}
