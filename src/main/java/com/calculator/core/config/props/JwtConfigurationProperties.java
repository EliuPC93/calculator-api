package com.calculator.core.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "calculator.jwt")
@Getter
@Setter
public class JwtConfigurationProperties {

    private int validity;
    private String secret;
    private String authHeader;
    private String bearer;

}
