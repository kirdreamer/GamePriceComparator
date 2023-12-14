package com.spielpreisvergleicher.common.exception;

public class IncorrectCredentialsException extends HttpException {
    public IncorrectCredentialsException(int code, String message) {
        super(code, message);
    }
}
