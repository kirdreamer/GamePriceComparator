package com.gamepricecomparator.common.exception;

public class IncorrectTokenException extends HttpException {
    public IncorrectTokenException(int code, String message) {
        super(code, message);
    }
}
