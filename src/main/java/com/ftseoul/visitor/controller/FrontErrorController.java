package com.ftseoul.visitor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftseoul.visitor.dto.FrontErrorResponseDto;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FrontErrorController {

    @PostMapping("/front-err")
    public ResponseEntity<FrontErrorResponseDto> logFrontError
        (@RequestBody Map<String, Object> frontError) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Front error: {}", objectMapper.writeValueAsString(frontError));
        return new ResponseEntity<>(new FrontErrorResponseDto("2000", "성공"), HttpStatus.OK);
    }
}
