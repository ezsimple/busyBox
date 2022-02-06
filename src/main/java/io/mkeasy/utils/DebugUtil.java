package io.mkeasy.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugUtil {

	public static void printParams(WebRequest webRequest) {
		Iterator<String> paramNames = webRequest.getParameterNames();
		while(paramNames.hasNext()) {
			String key = paramNames.next();
			String value = webRequest.getParameter(key);
			log.debug("{}={}",key,value);
		}
		return;
	}

	public static void printParams(Map<String, Object> map) {
		for (Entry<String, Object> entry : map.entrySet()) {
			log.debug("{}={}",entry.getKey(),entry.getValue());
		}
		return;
	}

}
