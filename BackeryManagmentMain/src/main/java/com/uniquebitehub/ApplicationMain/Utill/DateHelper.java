package com.uniquebitehub.ApplicationMain.Utill;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	public static Date getCurrentDate() {
		return new Date();
	}

	public static String getCurrentTimestampFormatted() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		return formatter.format(new Date());
	}
}