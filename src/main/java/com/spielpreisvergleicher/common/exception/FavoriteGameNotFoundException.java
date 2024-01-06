package com.spielpreisvergleicher.common.exception;

public class FavoriteGameNotFoundException extends HttpException {
    public FavoriteGameNotFoundException(int code, String message) {
        super(code, message);
    }
}
