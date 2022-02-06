package io.mkeasy.webapp.utils;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CookieUtils {
	private static String getCookie(Cookie[] cookies, String name) {

		for (int i = 0; i < cookies.length; i++) {
			if (java.net.URLDecoder.decode(cookies[i].getName()).equals(name)) {
				return java.net.URLDecoder.decode(cookies[i].getValue());
			}
		}
		return "";
	}

	public static String getCookie(HttpServletRequest request, String name, String defaultValue) {
		String val = getCookie(request, name);
		
		return StringUtils.isEmpty(val) ? defaultValue : val;
	}
	public static String getCookie(HttpServletRequest request, String name) {

		Cookie[] cookies = request.getCookies();

		if (cookies == null) return "";

		String value = getCookie(cookies, name);
		if (value.equals("")) return "";
		return value;
	}

	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
		setCookie(response, name, value, maxAge, null);
	}
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
		setCookie(response, name, value, maxAge, domain, true);
	}
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge, String domain, boolean isHttpOnly) {
		Cookie cookie = null;

		String cookieName = URLEncoder.encode(name);
		String cookieValue = URLEncoder.encode(value);

		cookie = new Cookie(cookieName, cookieValue);
		
		if(StringUtils.isNotEmpty(domain)){
			cookie.setDomain(domain);
		}
		cookie.setPath("/");
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		cookie.setHttpOnly(isHttpOnly);
		response.addCookie(cookie);
	}
	public static void removeCookie(HttpServletResponse response, String name) {
		Cookie cookie = null;

		String cookieName = URLEncoder.encode(name);

		cookie = new Cookie(cookieName, "");
		cookie.setPath("/");
		
		cookie.setMaxAge(0);
		

		response.addCookie(cookie);
	}

}
