package com.ftseoul.visitor.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@RefreshScope
public class EncryptConfig {

    @Value("${encrypt.seed}")
    private String key;

    @Value("${encrypt.seed.init}")
    private String IV;
}
