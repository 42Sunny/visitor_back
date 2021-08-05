package com.ftseoul.visitor.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@Slf4j
public class PhoneDuplicatedException extends RuntimeException{
    public PhoneDuplicatedException(String message) {
        super(message);
        log.error("Phone Number Duplicated");
    }
}
