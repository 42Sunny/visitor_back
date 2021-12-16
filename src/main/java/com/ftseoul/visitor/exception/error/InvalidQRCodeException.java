package com.ftseoul.visitor.exception.error;

import lombok.Getter;

@Getter
public class InvalidQRCodeException extends RuntimeException{
    private String code;
    private Object value;

    public InvalidQRCodeException(String code, Object value) {
        this.code = code;
        this.value = value;
    }
}
