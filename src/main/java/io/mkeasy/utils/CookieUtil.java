package io.mkeasy.utils;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void create(HttpServletResponse response, String name, String value, Boolean secure, Integer maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setHttpOnly(true);

        // maxAge negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits. 
        // A zero value causes the cookie to be deleted.
        cookie.setMaxAge(maxAge);

        // cookie.setDomain(domain); // 앞에 '.' 이 붙어서 셋팅이 된다. 그러면 삭제도 되지 않는다.

        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void clear(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String getValue(HttpServletRequest response, String name) {
        Cookie cookie = WebUtils.getCookie(response, name);
        return cookie != null ? cookie.getValue() : null;
    }
}
