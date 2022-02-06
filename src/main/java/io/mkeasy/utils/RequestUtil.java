package io.mkeasy.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RequestUtil {

	public static HttpServletRequest getHttpServletRequest() {
		return NetUtil.getHttpServletRequest();
	}
	
	public static String getURI() {
		HttpServletRequest req = getHttpServletRequest();
		return req==null?"":req.getServletPath();
	}

	public static JSONObject getParameterToJson(HttpServletRequest request) {
		return JSONObject.fromObject(request.getParameterMap());
	}

	public static JSONObject getParameterToJson(Map<String,Object> map) {
		return JSONObject.fromObject(map);
	}

	public static Map<String, Object> getEncodedMap(Map<String,Object> map) throws UnsupportedEncodingException {
		Map newMap = MapUtil.newMap();
		Iterator<String> keys = map.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			Object value = map.get(key);
			String encValue = URLEncoder.encode((String) value, "UTF-8");
			newMap.put(key, encValue);
		}
		return newMap;
	}

	// key에 해당하는 배열의 첫번째 스트링만을 리턴한다. (대다수의 경우)
	public static String getParameterToString(HttpServletRequest request, final String key) {
		JSONObject o = getParameterToJson(request);
		JSONArray  a = o.getJSONArray(key);
		return a.getString(0);
	}

	public static String getParameter(HttpServletRequest request, final String key) {
		return request.getParameter(key);
	}

	// ---------------------------------------------------------------------
	// post 방식에서 서로 다른 컨트롤러간 데이타 공유을 하기 위해서 인데,
	// 이 경우 사이드 이펙트가 존재하여 기능을 삭제한다.
	// get/set producer/consumer 가 항상 일치하지는 않는다.
	// ---------------------------------------------------------------------
	/*
	private final static String PARAMS = "__PARAMS__";
	public static void setParams(HttpServletRequest request) {
		JSONObject params = getParameterToJson(request);
		if (!params.isEmpty())
			request.getSession().setAttribute(PARAMS, params);
	}
	public static JSONObject getParams(HttpServletRequest request) {
		JSONObject params = (JSONObject) request.getSession().getAttribute(PARAMS);
		if(params==null || params.isEmpty())
			return null;
        request.getSession().removeAttribute(PARAMS);
		return params;
	}
	 */

	// radio, checkbox의 값을 true/false 로 변환한다.
	public static boolean convBoolean(String flag) {
		if("on".equals(flag))
			return true;
		return false;
	}

}