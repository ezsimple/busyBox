package io.mkeasy.utils;

import java.util.Map;

public class ResultUtil {

	private final static String[] keys = {"success","message"};
	
	public static Map<String, Object> newResult() {
		Map<String, Object>resultMap = MapUtil.newMap();
		setDefault(resultMap);
		return resultMap;
	}

	public static void setDefault(Map<String, Object> resultMap) {
		resultMap.put(keys[0], false);
		resultMap.put(keys[1], "요청이 실패 하였습니다.");
	}

	public static void init(Map<String, Object> resultMap) {
		setDefault(resultMap);
	}

	public static void setSuccess(Map<String, Object> resultMap, Boolean value) {
		final String key = keys[0];
		if (resultMap.containsKey(key)) {
			resultMap.remove(key);
			resultMap.put(key, value);
			return;
		}
		resultMap.put(key, value);
	}
	
	public static boolean isSuccess(Map<String, Object> resultMap) {
		final String key = keys[0];
		return (boolean) resultMap.get(key);
	}

	public static void setMessage(Map<String, Object> resultMap, String value) {
		final String key = keys[1];
		if (resultMap.containsKey(key)) {
			resultMap.remove(key);
			resultMap.put(key, value);
			return;
		}
		resultMap.put(key, value);
	}

	public static void setSuccessMessage(Map<String, Object> resultMap, String value) {
		setSuccess(resultMap, true);
		setMessage(resultMap, value);
	}
}