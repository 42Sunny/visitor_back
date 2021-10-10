package com.ftseoul.visitor.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class InvalidDeviceException extends RuntimeException{
    private final String deviceId;

    public InvalidDeviceException(String deviceId) {
        super(String.format("허용되지 않은 디바이스 : %s", deviceId));
        log.error("허용되지 않은 기기: {}", deviceId);
        this.deviceId = deviceId;
    }
}
