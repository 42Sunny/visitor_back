package com.ftseoul.visitor.exception.error;

import lombok.Getter;

@Getter
public class InvalidDeviceException extends RuntimeException{
    private String deviceId;

    public InvalidDeviceException(String deviceId) {
        this.deviceId = deviceId;
    }
}
