package com.ftseoul.visitor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthyController {
    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("서버 정상 작동 중");
    }
}
