package com.ftseoul.visitor.p6spyconfig;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("testConfig")
public class DatasourceProxyConfig implements BeanPostProcessor {


}
