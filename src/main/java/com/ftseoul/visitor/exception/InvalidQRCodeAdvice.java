package com.ftseoul.visitor.exception;

import com.ftseoul.visitor.dto.qrcode.QRCheckResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class InvalidQRCodeAdvice {

    private final String notFoundCode = "4040";
    private final String unAuthorizedCode = "4030";

    @ExceptionHandler(InvalidQRCodeException.class)
    @ResponseStatus(HttpStatus.OK)
    public QRCheckResponseDto InvalidQRCodeExceptionHandler(InvalidQRCodeException ex) {
        return new QRCheckResponseDto(notFoundCode, ex.getMessage(), "무효");
    }

    @ExceptionHandler(InvalidDeviceException.class)
    @ResponseStatus(HttpStatus.OK)
    public QRCheckResponseDto InvalidDeviceExceptionHandler(InvalidDeviceException ex) {
        return new QRCheckResponseDto(unAuthorizedCode, ex.getMessage(), "무효");
    }
}
