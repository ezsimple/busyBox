package io.mkeasy.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil2 {

	public static JSONObject getJSONObject(JSONObject innerObj, String key) {
		try {
			return (JSONObject) innerObj.get(key);
		} catch (Exception e) {
			return new JSONObject();
		}
	}

	public static JSONArray getJSONArray(JSONObject innerObj, String key) {
		try {
			return (JSONArray) innerObj.get(key);
		} catch (Exception e) {
			return new JSONArray();
		}
	}

	public static String getValue(JSONObject tmpObj, String key) throws Exception {
		boolean has = tmpObj.has(key);
		String defaultValue = null;
		String value = has ? String.valueOf(tmpObj.get(key)):defaultValue;

		if(StringUtils.equals(value, defaultValue))
			return value;

		if(StringUtils.equals(value, "null"))
			return defaultValue;

		return value;
	}

	public static String getValue(JSONObject tmpObj, String key, String defaultValue, boolean roundOk) {
		boolean has = tmpObj.has(key);
		String value = has ? String.valueOf(tmpObj.get(key)):defaultValue;

		if(StringUtils.equals(value, defaultValue))
			return value;

		if(StringUtils.equals(value, "null"))
			return defaultValue;

		if(!ValidUtil.isFloatFormat(value))
			return value;

		// 모든 숫자를 소숫점 처리할 것 인가?

		// 0.0은 0으로 처리합니다.
		if(StringUtils.equals(value, "0.0"))
			return "0";

		// .0로 끝이 나는 소숫점은 정수처리 합니다.
		if(ValidUtil.isZeroEndFormat(value))
			return String.valueOf(StringUtil.toInt(value));

		// 		// CJ김기홍님 요청사항 분석을 위해 당분간 소숫점 자리수 제한을 하지 않습니다. 2020.06.17
		//		// 소숫점 길이는 2자리로 제한합니다. (소숫점 3째자리 반올림 적용), 지수표현 제거
		// 종계보고서는 roundOk : true 입니다.
		// 육계보고서는 pointFeed(Act/Std), bodyWeight(Act/Std)는 roundOk: false 입니다.
		// -- 소숫점 그대로 표현합니다.
		if(roundOk)
			value = String.format("%.2f", (Math.round((Double.parseDouble(value)*100))/100.0));

		return value;
	}

}
