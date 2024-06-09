package com.calculator.core.security;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;

import com.calculator.core.config.props.CryptoConfigurationProperties;

import javax.annotation.PostConstruct;

@Service
public class CalculatorEncryptor {
    private AES256TextEncryptor encryptor;

    private CryptoConfigurationProperties cryptoConfigurationProperties;

    public CalculatorEncryptor(CryptoConfigurationProperties cryptoConfigurationProperties) {
        this.cryptoConfigurationProperties = cryptoConfigurationProperties;
    }

    @PostConstruct
    public void init() {
        encryptor = new AES256TextEncryptor();
        encryptor.setPassword(cryptoConfigurationProperties.getPassword());
    }

    public String encrypt(String rawString) {
        return encryptor.encrypt(rawString);
    }

    public String decrypt(String encryptedString) {
        return encryptor.decrypt(encryptedString);
    }
}
