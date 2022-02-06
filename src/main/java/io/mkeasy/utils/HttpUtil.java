package io.mkeasy.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import eu.bitwalker.useragentutils.Browser;

public class HttpUtil {
	public static HttpServletRequest getCurrentRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static HttpServletResponse getCurrentResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	public static String getJsonContentType(HttpServletRequest request) {
		Browser browser = AgentUtil.getBrowser(request);

		if (browser != null && browser == Browser.IE) {
			return "text/plain; charset=UTF-8";
		}

		return "application/json; charset=UTF-8";
	}

}
