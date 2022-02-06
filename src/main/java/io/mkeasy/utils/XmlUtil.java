package io.mkeasy.utils;

import org.json.JSONObject;
import org.json.XML;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlUtil {

	public static String toXML(String json) throws Exception {
		JSONObject jsonData = new JSONObject(json);
		String xml = XML.toString(jsonData);
		return xml;
	}

	public static String toXML(JSONObject json) throws Exception {
		return toXML(json.toString());
	}
}
