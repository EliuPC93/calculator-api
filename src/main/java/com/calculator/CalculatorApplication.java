package com.calculator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.calculator.data.entity")
@EnableJpaRepositories("com.calculator.core.repository")
@SpringBootApplication
@ComponentScan({"com.calculator.controller", "com.calculator.core"})
public class CalculatorApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CalculatorApplication.class);
        builder.build().addListeners(new ApplicationPidFileWriter());
        builder.run();
    }
}
