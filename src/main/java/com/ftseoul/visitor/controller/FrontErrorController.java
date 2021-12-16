package com.ftseoul.visitor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Map;

import com.ftseoul.visitor.exception.ErrorResponse;
import com.ftseoul.visitor.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Slf4j
public class FrontErrorController {

    @PostMapping("/front-err")
    public ResponseEntity<ErrorResponse> logFrontError(@RequestBody Map<String, Object> frontError) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Front error: {}", objectMapper.writeValueAsString(frontError));
        ErrorResponse response = ErrorResponse.of(ErrorCode.FRONT_ERRLOG_RECIEVE,new ArrayList<>());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
