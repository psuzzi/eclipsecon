package com.itemis.e4.banking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

	public static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Date stringToDate(String dateString) {
		return stringToDate(DF, dateString);
	}
	
	public static Date stringToDate(String dateString, Date defaultDate) {
		return stringToDate(DF, dateString, defaultDate);
	}

	public static Date stringToDate(DateFormat format, String dateString) {
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			// parse error
			System.err.format(
					"Error in parsing, %s is not a valid date format, it should be \"%s\".",
					dateString, format.toString());
			e.printStackTrace();
			return null;
		}
	}
	
	/** Parse the given string to date, if not successful, returns the default date */
	public static Date stringToDate(DateFormat format, String dateString, Date defaultDate) {
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			return defaultDate;
		}
	}
	
	public static String dateToString(Date date) {
		return dateToString(DF, date);
	}
	
	public static String dateToString(DateFormat format, Date date) {
		return format.format(date);
	}
	
	private static Logger logger;
	
	public static Logger getLogger() {
		if(logger == null) {
			System.setProperty(
					"java.util.logging.SimpleFormatter.format",
					"%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS [%4$s] %5$s%6$s%n");
			logger = Logger.getLogger("com.itemis.banking");
		}
		return logger;
	}
	
	public static void log(String message) {
		getLogger().log(Level.INFO, message);
	}
	
	public static void log(String message, Object ... params ) {
		getLogger().log(Level.INFO, String.format(message, params));
	}
	
	
}
