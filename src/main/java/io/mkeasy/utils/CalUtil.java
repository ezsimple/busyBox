package io.mkeasy.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalUtil {

	public final static long gmtTime(){
		return GregorianCalendar.getInstance().getTimeInMillis() ;
	}
	
	public final static Calendar newCalendar() {
		return Calendar.getInstance();
	}
	
	public final static int nowYear() {
		return newCalendar().get(Calendar.YEAR);
	}

	// Month값이 0~11 까지 나옴. ㄷㄷㄷ
	public final static int nowMonth() {
		return newCalendar().get(Calendar.MONTH)+1;
	}

	public final static int nowDay() {
		return newCalendar().get(Calendar.DAY_OF_MONTH);
	}
	
	public static void main(String[] args) {
		System.out.println(nowYear()+"/"+nowMonth()+"/"+nowDay());
	}

}
