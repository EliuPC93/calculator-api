package com.calculator.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class HealthCheckControllerTest extends BaseSpringBootIntegrationTest  {
    @Test
    public void healthCheck_should_returnHttpStatusOk_when_thereAreNoErrors() throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(serverUri+"/", String.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
