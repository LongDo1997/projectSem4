package com.aptech.boutquetShop.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
	public static final String REQUEST_MAPPING_PREFIX_API = "/api";
	public static final String VERSION_API_V1 = "/v1";
	public static final String REQUEST_MAPPING_PREFIX_WEB = "/web";
	public static final String MAXIMUM_DATE_STRING = "9999-12-31 23:59:59.000";

	public static final String getCurrentDateString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		String current = df.format(new Date());
		return current;
	}

	public static final Date getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		Date current = null;
		try {
			current = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return current;
	}
}
