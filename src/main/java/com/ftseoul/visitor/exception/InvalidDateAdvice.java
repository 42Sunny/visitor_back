package com.ftseoul.visitor.exception;

import com.ftseoul.visitor.dto.qrcode.QRCheckResponseDto;
import com.ftseoul.visitor.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class InvalidDateAdvice {
    private final WebSocketService webSocketService;
    private final String unAuthorizedCode = "4030";

    @ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(HttpStatus.OK)
    public QRCheckResponseDto invalidQRDateExceptionHandler(InvalidDeviceException ex) {
        webSocketService.sendMessageToSubscriber("/visitor", ex.getMessage());
        return new QRCheckResponseDto(unAuthorizedCode, ex.getMessage(), "무효");
    }
}
