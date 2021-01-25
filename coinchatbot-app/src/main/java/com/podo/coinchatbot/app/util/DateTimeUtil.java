package com.podo.coinchatbot.app.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@UtilityClass
public class DateTimeUtil {

    private final static DateTimeFormatter FULL_CONTEXT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String toDateTimeString(LocalDateTime now, long timeDifference) {
        LocalDateTime localDateTime = longToLocalDateTime(dateTimeToLong(now) + timeDifference);
        return localDateTime.format(DATETIME_FORMATTER);
    }

    public static LocalDateTime longToLocalDateTime(Long longValue) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), TimeZone.getDefault().toZoneId());
    }

    public static String toDateTimeString(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_FORMATTER);
    }

    public static String toDateString(LocalDateTime dateTime) {
        return DATE_FORMATTER.format(dateTime);
    }

    public static String toDateString(LocalDate date) {
        return DATE_FORMATTER.format(date);
    }

    public static Long dateTimeToLong(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String toFullContextString(LocalDateTime dateTime) {
        return dateTime.format(FULL_CONTEXT_FORMATTER);
    }

    public static String toFullContextString(LocalDate dateTime) {
        return dateTime.format(FULL_CONTEXT_FORMATTER);
    }

}
