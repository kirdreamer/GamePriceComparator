package com.gamepricecomparator.common.web.handler;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Objects;

@NoArgsConstructor
public class ExceptionHandlerUtils {

    public static String buildErrorMessage(Throwable throwable) {
        StringBuilder message = new StringBuilder(ExceptionUtils.getMessage(throwable));
        Throwable cause;
        if (Objects.nonNull(cause = throwable.getCause())) {
            message.append(" , cause: ").append(ExceptionUtils.getMessage(cause));
        }
        return message.toString();
    }
}
