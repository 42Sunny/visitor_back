package com.ftseoul.visitor.exception;

import com.ftseoul.visitor.exception.error.*;
import com.ftseoul.visitor.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import com.ftseoul.visitor.exception.ErrorResponse.FieldError;


@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final WebSocketService webSocketService;

    @ExceptionHandler(InvalidDateException.class)
    protected ResponseEntity<ErrorResponse> invalidQRDateExceptionHandler(InvalidDateException e) {
        webSocketService.sendMessageToSubscriber("/visitor", e.getMessage());
        log.warn("예약 날짜가 오늘이 아닙니다. 예약 날짜 : {}", e.getDate());
        ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED_CODE,new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidQRCodeException.class)
    protected ResponseEntity<ErrorResponse> invalidQRCodeExceptionHandler(InvalidQRCodeException e) {
        log.warn("유효하지 않은 QR코드 입니다. QR값 : {}", e.getCode());
        ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_QRCODE,new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(PhoneDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> phoneDuplicatedExceptionHandler(PhoneDuplicatedException e) {
        log.warn("중복되는 휴대전화 번호입니다. 다시 입력해주세요.");
        ErrorResponse response = ErrorResponse.of(ErrorCode.DUPLICATE_PHONE_NUMBER,new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.warn("Validation 오류입니다. 확인바랍니다.", e.getMessage());
        final List<FieldError> fieldErrors = FieldError.of(e.getBindingResult());
        ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED_CODE,fieldErrors);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        log.warn("해당 필드 {} 의 값 {}을 찿지 못했습니다.",e.getFieldName(),e.getFieldValue());
        final List<FieldError> fieldErrors = FieldError.bindResourceNotFoundException(e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND,fieldErrors);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(WAuthUnAuthorizedException.class)
    protected ResponseEntity<ErrorResponse> wAuthUnAuthorizedException(WAuthUnAuthorizedException e) {
        log.warn("w_auth인증에 실패하였습니다.");
        ErrorResponse response = ErrorResponse.of(ErrorCode.WAUTH_AUTHORIZED_FAIL,new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidDeviceException.class)
    protected ResponseEntity<ErrorResponse> invalidDeviceExceptionHandler(InvalidDeviceException e) {
        log.error("허용되지 않은 기기 : {}",e.getDeviceId());
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_DEVICE,new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        log.error("서버내부 오류입니다. 에러의 내용은 다음과 같습니다. 에러 내용 : {}",e.getMessage());
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
