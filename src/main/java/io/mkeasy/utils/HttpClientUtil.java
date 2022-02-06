package io.mkeasy.utils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.config.SocketConfig.Builder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class HttpClientUtil {

	static final int timeOut = 5000;
	private static CloseableHttpClient httpClient = null;
	private static Object syncLock = new Object();

	private static void setHttpHeaders(HttpRequestBase httpRequestBase) {
		httpRequestBase.addHeader("Content-Type", "application/json");
		httpRequestBase.addHeader("Accept-Charset","UTF-8");
		httpRequestBase.addHeader("Accept","application/json");	
	}

	private static RequestConfig getRequestConfig() {
		RequestConfig requestConfig = RequestConfig
				.custom()
				// .setConnectionRequestTimeout(timeOut)
				.setConnectTimeout(timeOut)
				// .setSocketTimeout(timeOut)
				.build();
		return requestConfig;
	}

	private static CloseableHttpClient getHttpClient(String url) throws MalformedURLException {

//		URL _url = new URL(url);

//		final int maxTotal = 1024;
//		final int maxPerRoute = 32;
//		final int maxRoute = 64;

		// return httpClient = createHttpClient(maxTotal, maxPerRoute, maxRoute, _url);
		return createHttpClient();
	}

	public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, URL url) {

		HttpHost httpHost = new HttpHost(url.getHost(), url.getPort());
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(maxTotal);
		cm.setDefaultMaxPerRoute(maxPerRoute);
		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

		// HttpRequestRetryHandler retryHandler = getRetryHandler();
		
		// ConnectionKeepAliveStrategy keepAlive = getKeepAliveStrategy();

		RequestConfig config = getRequestConfig();
		CloseableHttpClient httpClient = 
				  HttpClientBuilder
				  .create()
				  .setDefaultRequestConfig(config)
				  .setConnectionManager(cm)
				  // .setRetryHandler(retryHandler)
				  // .setKeepAliveStrategy(keepAlive)
				  .build();

		return httpClient;
	}

	private static CloseableHttpClient createHttpClient() {
		
		BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
		SocketConfig socketConfig = cm.getSocketConfig().custom()
				// .setSoKeepAlive(false)
				// .setSoReuseAddress(true)
				// .setSoTimeout(10000)
				.setTcpNoDelay(true)
				.build();
		cm.setSocketConfig(socketConfig);

		RequestConfig config = getRequestConfig();
		CloseableHttpClient httpClient = 
				  HttpClientBuilder
				  .create()
				  .setDefaultRequestConfig(config)
				  .setConnectionManager(cm)
				  .setKeepAliveStrategy((response, context) -> 0)
				  .build();

		return httpClient;
	}

	private static ConnectionKeepAliveStrategy getKeepAliveStrategy() {
		ConnectionKeepAliveStrategy keepAlive = new ConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				return timeOut; // Socket timeout
			}
		};
		return keepAlive;
	}

	private static HttpRequestRetryHandler getRetryHandler() {
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 3) {
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					return false;
				}
				if (exception instanceof InterruptedIOException) {
					return false;
				}
				if (exception instanceof UnknownHostException) {
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {
					return false;
				}
				if (exception instanceof SSLException) {
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();

				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};
		return httpRequestRetryHandler;
	}

	private static void setPostParams(HttpPost httpost, Map<String, Object> params) throws UnsupportedEncodingException {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
		}
        httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
	}

	// net.sf.json.JSONObject은 Map과 구분되지 않습니다.
//	private static void setPostParams(HttpPost httpost, String params) throws UnsupportedEncodingException {
//		if(params == null) return;
//		ByteArrayEntity entity = new ByteArrayEntity(params.toString().getBytes("UTF-8"));
//        httpost.setEntity(entity);
//	}

	public static String post(String url, Map<String, Object> params) throws Exception {

		HttpPost httppost = new HttpPost(url);
		setHttpHeaders(httppost);
		setPostParams(httppost, params);
		CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
		try {
			client = getHttpClient(url);
			response = client.execute(httppost, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} finally {
			IOUtils.closeQuietly(response);
			IOUtils.closeQuietly(client);
		}
	}
	
	private static String parseURL(String url) throws Exception {
		if(!StringUtils.startsWith(url, "http")) {
			final String query = url;
			final String host = NetUtil.getHost();
			return host + url;
		}
		return url;
	}

	public static String post(String url, JSONObject params) throws Exception {
		HttpPost httppost = new HttpPost(parseURL(url));
		setHttpHeaders(httppost);
		setPostParams(httppost, params);
		CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
		try {
			client = getHttpClient(url);
			response = client.execute(httppost, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} finally {
			IOUtils.closeQuietly(response);
			IOUtils.closeQuietly(client);
		}
	}

	public static String get(String url) throws Exception {
		HttpGet httpget = new HttpGet(parseURL(url));
		setHttpHeaders(httpget);
		CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
		try {
			client = getHttpClient(url);
			response = client.execute(httpget, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} finally {
			IOUtils.closeQuietly(response);
			IOUtils.closeQuietly(client);
		}
	}

}
