package io.mkeasy.utils;

import org.apache.commons.lang3.StringUtils;

public class NumberUtil {
	
	public static String toRound(double value, int decPoint) throws Exception {
		return toRound(String.valueOf(value), decPoint);
	}

	public static String toRound(String value, int decPoint) throws Exception {
		
		if(decPoint < 0) // 소숫점 자리수 표현은 반드시 0보다 같거나 커야 합니다.
			throw new Exception("decPoint는 0보다 작을 수 없습니다.");

		if(StringUtils.isEmpty(value)) { 
			return "";
		}
		
		// decPoint가 0일 경우 정수로 반환
		if(decPoint == 0) {
			double valueRounded = Math.round(Double.parseDouble(value));
			String v = String.valueOf(valueRounded);
			return String.valueOf(StringUtil.toInt(v));
		}
		
		// 자리수가 맞을 경우는 소숫점 자리수 변환 작업 안함
		String[] fVal = StringUtils.split(value, '.');
        if(fVal[1]!=null && fVal[1].length() == decPoint)
            return value;
        
		// 자리수가 맞지 않을 경우 강제로 맞춰달라.
        int mod_a = (int) Math.pow(10,decPoint+1);
        double mod_b = Math.pow(10.0,decPoint+1);

        double valueRounded = (Math.round(Double.parseDouble(value)*mod_a)/mod_b);
        String v = String.valueOf(valueRounded);
        v = String.format("%."+decPoint+"f", valueRounded);

        return v;
	}

	// 정수로만 표기 
	public static String toInt(String value) throws Exception {
		return toRound(value, 0);
	}

	public static String toInt(double value) throws Exception {
		return toInt(String.valueOf(value));
	}

	// decPoint 소숫점 이하 자리수
	public static String toDouble(double value, int decPoint) throws Exception {
		return toDouble(String.valueOf(value), decPoint);
	}
	
	// decPoint 소숫점 이하 자리수
	public static String toDouble(String value, int decPoint) throws Exception {

		// decPoint가 0일 경우 정수로 반환
		if(decPoint == 0) {
			double valueRounded = Double.parseDouble(value);
			String v = String.valueOf(valueRounded);
			return String.valueOf(StringUtil.toInt(v));
		}
		
		// 자리수가 맞을 경우는 소숫점 자리수 변환 작업 안함
		String[] fVal = StringUtils.split(value, '.');
        if(fVal[1]!=null && fVal[1].length() == decPoint)
            return value;
        
		// 자리수가 맞지 않을 경우 강제로 맞춰달라.
        int mod_a = (int) Math.pow(10,decPoint+1);
        double mod_b = Math.pow(10.0,decPoint+1);

        double valueRounded = (Double.parseDouble(value)*mod_a)/mod_b;
        String v = String.valueOf(valueRounded);
        v = String.format("%."+decPoint+"f", valueRounded);

        return v;
	}

}
