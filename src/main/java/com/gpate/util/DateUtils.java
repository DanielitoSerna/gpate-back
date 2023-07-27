package com.gpate.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static String convertDateToString(Date fecha) {
		DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String currentDateTime = dateFormatter.format(fecha);
		
		return currentDateTime;
	}

}
