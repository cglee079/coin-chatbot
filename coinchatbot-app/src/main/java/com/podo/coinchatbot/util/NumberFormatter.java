package com.podo.coinchatbot.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class NumberFormatter {

    public static String toNumberStr(double value, int length) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(0);
        df.setMinimumIntegerDigits(length);
        return df.format(value);
    }

}
