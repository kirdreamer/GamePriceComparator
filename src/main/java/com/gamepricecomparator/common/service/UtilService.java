package com.gamepricecomparator.common.service;

import java.util.Locale;

public class UtilService {

    private UtilService() {
        throw new IllegalStateException("UtilService");
    }

    public static String filterSymbolsInName(String name) {
        return name.replaceAll("[^\\w ]", "");
    }

    public static Double keepTwoDigitAfterDecimal(double value) {
        return Double.valueOf(String.format(Locale.US, "%.2f", value));
    }
}
