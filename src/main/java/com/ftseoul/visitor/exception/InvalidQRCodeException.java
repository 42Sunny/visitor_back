package com.ftseoul.visitor.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class InvalidQRCodeException extends RuntimeException{
    private final String code;
    private final Object value;

    public InvalidQRCodeException(String code, Object value) {
        super(String.format("유효하지 않은 QRCode %s : '%s'", code,
            value));
        log.warn("유효하지 않은 QRCode" + code + ": '" + value + "'");
        this.code = code;
        this.value = value;
    }
}
