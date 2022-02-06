package io.mkeasy.webapp.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

@Slf4j
public class TimeUtils {
	private static SimpleDateFormat defFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat gtmFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	public static int getMinute(String time) {
		
		time = time.substring(2);
		time = time.replaceAll("M", "");
		
		if(time.indexOf("H")>0){
			String[] tm = time.split("H");
			if(tm.length==1){
				return Integer.parseInt(tm[0]) * 60;
			}else{
				return Integer.parseInt(tm[0]) * 60 + Integer.parseInt(tm[1]);
			}
		}else{
			return Integer.parseInt(time);			
		}
	}
	
	public static String getDateGMT(String time)throws Exception {
		gtmFormat.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		Date date = gtmFormat.parse(time);

		//System.out.println(time);
		//System.out.println(defFormat.format(date));
		return defFormat.format(date);
	}
	public static String getDate(String time)throws Exception {
		time = StringUtils.substringBefore(time, "+");
		time = StringUtils.replace(time, "T", " ");
		
		return time;
	}
	public static String getGTMDate(Date date)throws Exception {
		gtmFormat.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		return gtmFormat.format(date);
	}
	
	public static void main(String[] args) throws Exception{
		String date = getDateGMT("2015-03-10T23:16:49Z");
		System.out.println(date);
		System.out.println(getGTMDate(new Date()));
	}
/*
 *		Date d = (Date) o;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		serialize(new BasicSearchObject("$date", format.format(d)), buf);

 */
}
