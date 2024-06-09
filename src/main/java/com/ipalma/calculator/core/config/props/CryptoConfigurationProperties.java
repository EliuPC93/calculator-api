package com.ipalma.calculator.core.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "calculator.security.crypto")
public class CryptoConfigurationProperties {
    private String password;
}
