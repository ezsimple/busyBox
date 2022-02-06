package io.mkeasy.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
public class NetUtil {

	public static String getIp() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		return ip.getHostAddress();
	}

	public static String getMAC() throws UnknownHostException, SocketException {
		InetAddress ip = InetAddress.getLocalHost();
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mac = network.getHardwareAddress();
		if (mac == null) return "mac address is null";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : "").toUpperCase());
		}
		String macAddress = sb.toString();
		// log.debug("IP : {}, MAC : {}",ip.getHostAddress(), macAddress);
		return macAddress;
	}

	// http://localhost:8100 => return "localhost"
	public static String getDomain() {
		HttpServletRequest request = getHttpServletRequest();
		String host = request.getServerName();
		return host;
	}

	public static String getHost(ServletRequest request) {
		String host = request.getServerName();
		int port = request.getServerPort();
		String scheme = request.getScheme();
		ServletContext ctx = request.getServletContext();
		String _ctx = ctx.getContextPath();
		String hostname = scheme+"://"+host+":"+port+"/"+_ctx;
		return hostname;
	}

	public static String getHost() throws Exception {
		final HttpServletRequest request = getHttpServletRequest();
		if(request!=null) {
			final String host = request.getServerName();
			final int port = request.getServerPort();
			final String scheme = request.getScheme();
			final String ctx = request.getContextPath();
			final String hostname = scheme+"://"+host+":"+port+"/"+ctx;
			return hostname;
		}

		final String hostname = getTomcatServerInfo();
		return hostname;
	}

	public static String getPagename() {
		HttpServletRequest req = getHttpServletRequest();
		String uri = req.getRequestURI();
		log.debug("uri : {}", uri);
		if(StringUtils.isEmpty(uri)) {
			log.warn("uri is empty");
			return "";
		}
		String pageName = String.valueOf(uri.subSequence(uri.lastIndexOf("/")+1, uri.lastIndexOf('.')));
		if(StringUtils.isEmpty(pageName)) {
			log.warn("pageName is empty");
			return "";
		}
		return pageName;

	}

	public static String getClientIP() {
		HttpServletRequest req = getHttpServletRequest();
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null) ip = req.getHeader("Proxy-Client-IP");
		if (ip == null) ip = req.getHeader("WL-Proxy-Client-IP");
		if (ip == null) ip = req.getHeader("HTTP_CLIENT_IP");
		if (ip == null) ip = req.getHeader("HTTP_X_FORWARDED_FOR");
		if (ip == null) ip = req.getRemoteAddr();
		if(StringUtils.equals(ip, "0:0:0:0:0:0:0:1")
				||StringUtils.equals(ip, "::1"))
			ip = "127.0.0.1";
		return ip;
	}

	public static HttpServletRequest getHttpServletRequest() {
		RequestAttributes request = RequestContextHolder.getRequestAttributes();
		if(request==null) return null;
		return ((ServletRequestAttributes)request).getRequest();
	}

	// Thread Safe 
	public static JSONObject get(String url) throws Exception {
		String response = HttpClientUtil.get(url);
		return new JSONObject().fromObject(response); // caution : dealloc memory
	}

	// Thread Safe 
	public static JSONObject post(final String url, JSONObject params) throws Exception {
		String response = HttpClientUtil.post(url, JSONUtil.toMap(params));
		// cmd_type=S의 경우 response가 별도 없습니다.
		JSONObject ret = null;
		if(StringUtils.isEmpty(response))
			return null;

		try {
			ret = new JSONObject().fromObject(response);
		} catch (Exception e) {
			log.debug("url : {}", url);
			log.debug("req : {}", params);
			log.error("res : {}", response);
			throw e;
		}
		return ret; // caution : dealloc memory
	}



	public static String getTomcatServerInfo() throws Exception {

		MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
				Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
		String host = InetAddress.getLocalHost().getHostAddress();
		String port = objectNames.iterator().next().getKeyProperty("port");

		// context 정보는 가져오지 못했습니다.
		return ("http://"+host+":"+port);

	}

	@Cacheable(value="IotCache", keyGenerator="keyGenerator")
	public static String getHostname() {

		InetAddress ip;
		String hostname = "no_name_host";
		try {
			ip = InetAddress.getLocalHost();
			hostname = ip.getHostName();
		} catch (Exception e) { }

		return hostname;
	}
	
	// InetAddress 객체 생성을 위해 필요.
	public static byte[] ip2bytes(String ip) {
		byte[] bytesIp;
		try {
			bytesIp = InetAddress.getByName(ip).getAddress();
		} catch (UnknownHostException e) {
			bytesIp = new byte[4];  // fall back to invalid 0.0.0.0 address
		}
		return bytesIp;
	}


	@Test
	public void Test() throws UnknownHostException, SocketException {
		getMAC();
	}

}
