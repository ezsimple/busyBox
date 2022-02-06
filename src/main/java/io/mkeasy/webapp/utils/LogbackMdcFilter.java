package io.mkeasy.webapp.utils;

import io.mkeasy.utils.AgentUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.MDC;

import java.io.IOException;

public class LogbackMdcFilter implements Filter {

	final public String SESSION_USER = "user";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		RequestWrapper requestWrapper = RequestWrapper.of(request);

		// Set Http Header
		MDCUtil.setJsonValue(MDCUtil.HEADER_MAP_MDC, requestWrapper.headerMap());

		// Set Http Body
		MDCUtil.setJsonValue(MDCUtil.PARAMETER_MAP_MDC, requestWrapper.parameterMap());

		// If you use SpringSecurity, you could SpringSecurity UserDetail Information.

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		Object user = null;
		if (session != null) user = session.getAttribute(SESSION_USER);

		MDCUtil.setJsonValue(MDCUtil.USER_INFO_MDC, user);

		// Set Agent Detail
		MDCUtil.setJsonValue(MDCUtil.AGENT_DETAIL_MDC, AgentUtil.getAgentDetail((HttpServletRequest) request));

		// Set Http Request URI
		MDCUtil.set(MDCUtil.REQUEST_URI_MDC, requestWrapper.getRequestUri());

		try {
			chain.doFilter(request, response);
		} finally {
			MDC.clear();
		}
	}

	@Override
	public void destroy() {

	}
}