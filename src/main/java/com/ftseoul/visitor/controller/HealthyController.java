package com.ftseoul.visitor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RefreshScope
public class HealthyController {

    @Value("${version}")
    private String version;

    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok(version);
    }
}
