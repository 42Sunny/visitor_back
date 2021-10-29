package com.ftseoul.visitor.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String date) {
        super("오늘 예약된 방문자가 아닙니다. 예약날자: " + date);
        log.info("오늘 예약된 방문자가 아닙니다. 예약날자: " + date);
    }
}
