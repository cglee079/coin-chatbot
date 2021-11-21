package com.podo.coinchatbot.app.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class NumberUtil {

    public static boolean eq(BigDecimal x, BigDecimal y) {
        if (x == null && y == null) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        return x.doubleValue() == y.doubleValue();
    }

}
