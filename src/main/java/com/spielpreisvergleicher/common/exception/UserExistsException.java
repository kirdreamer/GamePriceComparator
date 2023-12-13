package com.spielpreisvergleicher.common.exception;

public class UserExistsException extends HttpException {
    public UserExistsException(int code, String message) {
        super(code, message);
    }
}
