package com.ftseoul.visitor.exception;

import com.ftseoul.visitor.dto.error.ErrorResponseDto;
import com.ftseoul.visitor.dto.payload.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ResourceNotFoundAdvice {

    private final String notFoundCode = "4040";

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponseDto ResourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        return new ErrorResponseDto(new Response(notFoundCode, ex.getMessage()));
    }
}
