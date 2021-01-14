package com.podo.coinchatbot.telegram.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class TimeStamper {

	public final static SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

	public static String getDateTime() {
		return DATETIME_FORMATTER.format(new Date());
	}

	public static String getDateTime(long localTime) {
		Date currentDate = new Date();
		currentDate.setTime(currentDate.getTime() + localTime);
		return DATETIME_FORMATTER.format(currentDate);
	}

	public static String getDateTime(Date date) {
		return DATETIME_FORMATTER.format(date);
	}

	public static String getDate() {
		return DATE_FORMATTER.format(new Date());
	}

	public static String getDateBefore() {
		Date d = new Date();
		d.setDate(d.getDate() - 1);
		return DATE_FORMATTER.format(d);
	}

}
