package com.spielpreisvergleicher.common.exception;

public class UserNotFoundException extends HttpException {
    public UserNotFoundException(int code, String message) {
        super(code, message);
    }
}
