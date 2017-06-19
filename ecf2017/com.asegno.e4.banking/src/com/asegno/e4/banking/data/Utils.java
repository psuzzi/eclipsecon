package com.asegno.e4.banking.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


public class Utils {

	private static final String DF_REGEX = "([0-9]{4})-([0-9]{2})-([0-9]{2})";
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

	/** Parse the given string to date, if not successeful, returns the default date */
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

	public static boolean isValidDate(String dateString) {
		try {
			Date d = stringToDate(dateString);
			return d!=null;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public static void resizeColumns(Table table) {
		int totalWidth = table.getClientArea().width;
		int floorDiv = Math.floorDiv(totalWidth, table.getColumnCount());
		for(TableColumn tc : table.getColumns()) {
			tc.setWidth(floorDiv);
		}
	}
	


}
