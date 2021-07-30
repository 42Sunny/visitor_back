package com.ftseoul.visitor.exception;

import com.ftseoul.visitor.dto.ErrorMessage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        final BindingResult br = exception.getBindingResult();
        final List<FieldError> fieldErrors = br.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            log.error("field: " + fieldError.getField() + "\n"
                    + "message: " + fieldError.getDefaultMessage() + "\n"
                    + "rejectedValue: " + fieldError.getRejectedValue());
        }

      return new ErrorMessage(fieldErrors
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList()));
    }
}
