package io.mkeasy.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
public class ProxyUtil {
    public static String getHtml(String url)throws Exception {
        Document doc = Jsoup.connect(url).get();
        for(Element e : doc.select("a"))
            e.attr("href", e.absUrl("href"));
        for(Element e : doc.select("img"))
            e.attr("src", e.absUrl("src"));
        return doc.html();
    }
    public static String getText(String url, String field)throws Exception {
        Document doc = Jsoup.connect(url).get();
        return doc.select(field).text();
    }
    public static String postText(String url, String field)throws Exception {
        Document doc = Jsoup.connect(url).post();
        return doc.select(field).text();
    }
    public static String getText(String url, Map<String, Object> parameterMap, String field)throws Exception {
		StringBuilder buf = new StringBuilder();
		if (parameterMap != null && !parameterMap.isEmpty()) {
			for (String key : parameterMap.keySet()) {
				String val = parameterMap.get(key).toString();
				val =  URLEncoder.encode(val,"UTF-8");
				buf.append(key+"="+val);
				buf.append("&");
			}
		}
		String query = StringUtils.removeEnd(buf.toString(), "&");
		url += "?"+query;
		log.info(url);
        String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
        log.info(json);
        return getResAsJson(json).get(field).getAsString();
    }

    // WiseBot 연동 메소드이다.
    // parameter가 empty 일 경우 : sessionId를 준다. answer에는 인사글이 존재한다.
    // parameter가 존재할 경우 : sessionId & msg 의 값을 셋팅하여 전달한다.
    public static JSONObject getWiseBotResponse(String _url, Map<String, String> parameterMap) throws Exception {
        URL url = new URL(_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        
        JSONObject o = new JSONObject();
		if (parameterMap != null && !parameterMap.isEmpty()) {
			for (String key : parameterMap.keySet()) {
				String value = parameterMap.get(key).toString();
				value =  URLEncoder.encode(value,"UTF-8");
				o.put(key, value);
			}
		}
		String query = o.toString();
		log.debug(query);
        OutputStream os = conn.getOutputStream();
        os.write(query.getBytes("UTF-8"));
        os.close();

        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = IOUtils.toString(in, "UTF-8");
        JSONObject jsonObject = new JSONObject().fromObject(result);
        conn.disconnect();
        return jsonObject;
    }

    public static String postText(String url, Map<String, String> parameterMap, String field)throws Exception {
		StringBuilder buf = new StringBuilder();
//		JsonObject param = new JsonObject();
//		if (parameterMap != null && !parameterMap.isEmpty()) {
//			for (String key : parameterMap.keySet()) {
//				String value = parameterMap.get(key).toString();
//				value =  URLEncoder.encode(value,"UTF-8");
//				param.addProperty(key, value);
//			}
//		}
		log.info(url);
        String json = Jsoup.connect(url)
        		.ignoreContentType(true)
        		.execute()
        		.body();
        log.info(json);
        return getResAsJson(json).get(field).getAsString();
    }
    private static JsonObject getResAsJson(String response) {
        return new JsonParser().parse(response).getAsJsonObject();
    }
}
