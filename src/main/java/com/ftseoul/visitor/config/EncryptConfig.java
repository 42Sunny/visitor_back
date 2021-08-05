package com.ftseoul.visitor.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
public class EncryptConfig {

    @Value("${encrypt.seed}")
    private String key;

    @Value("${encrypt.seed.init}")
    private String IV;
}
