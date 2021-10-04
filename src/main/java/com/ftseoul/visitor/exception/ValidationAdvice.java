package com.ftseoul.visitor.exception;

import com.ftseoul.visitor.dto.error.ErrorValidationDto;
import com.ftseoul.visitor.dto.payload.ValidationErrorResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorValidationDto methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        final BindingResult br = exception.getBindingResult();
        final List<FieldError> fieldErrors = br.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            log.warn("field: " + fieldError.getField() + "\t"
                    + "message: " + fieldError.getDefaultMessage() + "\t"
                    + "rejectedValue: " + fieldError.getRejectedValue());
        }

      return new ErrorValidationDto(fieldErrors
            .stream()
            .map(v -> new ValidationErrorResponse(v.getDefaultMessage()))
            .collect(Collectors.toList()));
    }

    @ExceptionHandler(PhoneDuplicatedException.class)
    public String phoneNumberDuplicatedExceptionHandler(Exception ex) {
        return "전화번호가 중복되었습니다";
    }
}
