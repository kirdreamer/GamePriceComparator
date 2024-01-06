package com.gamepricecomparator.common.web.handler;

import com.gamepricecomparator.common.exception.HttpException;
import com.gamepricecomparator.common.web.response.ErrorResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponse> handleHttpExceptions(@NonNull final HttpException exception) {
        log.error(exception.getMessage());

        return ResponseEntity.status(exception.code)
                .body(new ErrorResponse(exception.code, createErrorMessage(exception)));
    }

    private String createErrorMessage(HttpException exception) {
        final String message = exception.getMessage();
        log.error(ExceptionHandlerUtils.buildErrorMessage(exception));
        return message;
    }
}
