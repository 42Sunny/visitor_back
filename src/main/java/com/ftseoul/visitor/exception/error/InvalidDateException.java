package com.ftseoul.visitor.exception.error;

import lombok.Getter;

@Getter
public class InvalidDateException extends RuntimeException {

    private String date;

    public InvalidDateException(String date) {
        this.date = date;
    }
}
