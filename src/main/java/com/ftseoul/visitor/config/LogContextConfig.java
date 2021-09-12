package com.ftseoul.visitor.config;

import ch.qos.logback.classic.LoggerContext;
import com.ftseoul.visitor.appender.CustomLogbackAppender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogContextConfig implements InitializingBean {

    @Autowired
    LogConfig logConfig;

    @Override
    public void afterPropertiesSet() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        CustomLogbackAppender customLogbackAppender = new CustomLogbackAppender(logConfig);

        customLogbackAppender.setContext(loggerContext);
        customLogbackAppender.setName("customLogbackAppender");
        customLogbackAppender.start();
        loggerContext.getLogger("Root").addAppender(customLogbackAppender);
    }
}
