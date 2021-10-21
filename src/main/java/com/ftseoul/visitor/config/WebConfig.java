package com.ftseoul.visitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String[] allowedOrigins = {
        "http://localhost:3000",
        "https://visitor.dev.42seoul.io",
        "https://admin.dev.42seoul.io",
        "https://visitor.42seoul.io",
        "https://admin.42seoul.io"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("GET", "OPTIONS", "POST", "HEAD", "DELETE", "PUT", "PATCH")
                .maxAge(3600)
                .allowCredentials(true)
                .allowedOrigins(allowedOrigins);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
    }
}
