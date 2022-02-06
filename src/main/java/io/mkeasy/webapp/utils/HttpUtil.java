package io.mkeasy.webapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

public class HttpUtil {
	private static final int BUFFER_SIZE = 4 * 1024;

	public static String getString(String url, Map<String, Object> parameterMap)throws Exception {
		return getString(url, parameterMap, 5000);
	}
	public static String getString(String url, Map<String, Object> parameterMap, int timeout)throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		getByteArrayOutputStream(url, parameterMap, baos, timeout);
		String charset = (String)parameterMap.get("charset");
		if(StringUtils.isNotEmpty(charset)){
			return baos.toString(charset);
		}else{
			return baos.toString();
		}
	}
	public static File getFile(String url, Map<String, Object> parameterMap, File f)throws Exception {
		return getFile(url, parameterMap, f, 10000);
	}
	public static File getFile(String url, Map<String, Object> parameterMap, File f, int timeout)throws Exception {
		FileOutputStream fos = new FileOutputStream(f);
		try {
			getByteArrayOutputStream(url, parameterMap, fos, timeout);
		} finally {
			try {
				fos.close();;
			} catch (Exception e) {}
		}
		return f;
	}
	private static void getByteArrayOutputStream(String url, Map<String, Object> parameterMap, OutputStream os, int timeout)throws Exception {

		HttpMethodBase httpMethod = null;
		
		try {
			httpMethod = getHttpMethodBase(url, parameterMap, timeout);
			InputStream is = httpMethod.getResponseBodyAsStream();
			byte[] b = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = is.read(b)) > 0) {
				os.write(b, 0, len);
			}

		} finally {
			try {
				httpMethod.releaseConnection();
			} catch (Exception e) {}
		}
	}

	private static HttpMethodBase getHttpMethodBase(String url,
			Map<String, Object> parameterMap, int timeout) throws Exception {

		//Protocol.registerProtocol("https", new Protocol("https", new SSLProtocolSocketFactory(), 443));
		HttpClient httpClient = new HttpClient();
		
		if(0>timeout)httpClient.setTimeout(timeout);
		
		HttpMethodBase httpMethod = null;

		if (parameterMap == null || parameterMap.isEmpty()) {
			httpMethod = new GetMethod(url);
		} else {
			httpMethod = new PostMethod(url);
		}

		httpMethod.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=euc-kr");

		if (parameterMap != null && !parameterMap.isEmpty()) {
			for (String key : parameterMap.keySet()) {
				if(parameterMap.get(key) == null){
					((PostMethod) httpMethod).addParameter(key, "");
				}else{
					Object obj =  parameterMap.get(key);
					if (obj instanceof List) {
						List<String> vals = (List<String>) obj;
						for(String val : vals){
							((PostMethod) httpMethod).addParameter(key, val);
						}
					}else{
						((PostMethod) httpMethod).addParameter(key, obj.toString());
					}
				}
			}
		}

		try {
			httpClient.executeMethod(httpMethod);

		} catch (Exception e) {
			throw e;
		}

		if (httpMethod.getStatusCode() != HttpStatus.SC_OK) {
			String message = "Http Status Code is not OK : "
					+ httpMethod.getStatusCode()
					+ "\n url : " + url
					+ "\n params :" + parameterMap.toString();

			IOException e = new IOException(message);
			throw e;
			// PopupMessage.openError(e, Display.getCurrent().getActiveShell(),
			// "서버 접속에 실패하였습니다./n서버가 정상인지 확인하세요.");
		}

		return httpMethod;
	}

	
	public static void main(String[] args) throws Exception {
		String path = "http://125.140.110.206:8888/bspla/histories";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("venid", "1223081447");
		param.put("meterid", "3048501223081447***");
		//param.put("dataType", "ismart");
		param.put("dataType", "smartmeter");
		param.put("dateStr", "20150115");
		String html = HttpUtil.getString(path, param);
		System.out.println(html);
	}
}
