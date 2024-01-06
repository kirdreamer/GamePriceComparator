package com.gamepricecomparator.common.exception;

public class HttpException extends RuntimeException {
    public int code;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }
}
