package io.mkeasy.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getResponseAsMap(String responseBody)  {

		Map<String, Object> values = MapUtil.newMap();
		ObjectMapper mapper = new ObjectMapper();
		try {
			values = mapper.readValue(responseBody, Map.class);
		} catch (JsonParseException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}		
		return values;
	}

}