package com.gamepricecomparator.common.exception;

public class FavoriteGameNotFoundException extends HttpException {
    public FavoriteGameNotFoundException(int code, String message) {
        super(code, message);
    }
}
