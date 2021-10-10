package com.ftseoul.visitor.exception;

import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.util.MDCUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class CustomExceptionResponseHandler extends ResponseEntityExceptionHandler {

    public CustomExceptionResponseHandler() {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
        HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
        ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
        ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
        HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
        MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
        AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status,
        WebRequest webRequest) {
        log.error(ex.getMessage());
        Map<String,String> response = makeResponsePayload(ex, status);
        return this.handleExceptionInternal(ex, response, headers, status, webRequest);
    }

    private Map<String, String> makeResponsePayload(Exception ex, HttpStatus status) {
        Map<String,String> response = new HashMap<>();
        response.put("status", String.valueOf(status.value()));
        response.put("message", ex.getLocalizedMessage());
        response.put("url", MDCUtil.get(MDCUtil.REQUEST_URI_MDC));
        return response;
    }
}
