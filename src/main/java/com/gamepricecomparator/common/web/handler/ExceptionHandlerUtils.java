package com.gamepricecomparator.common.web.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Objects;

public class ExceptionHandlerUtils {

    private ExceptionHandlerUtils() {
        throw new IllegalStateException("ExceptionHandlerUtils");
    }

    public static String buildErrorMessage(Throwable throwable) {
        StringBuilder message = new StringBuilder(ExceptionUtils.getMessage(throwable));
        Throwable cause = throwable.getCause();
        if (Objects.nonNull(cause)) {
            message.append(" , cause: ").append(ExceptionUtils.getMessage(cause));
        }
        return message.toString();
    }
}
