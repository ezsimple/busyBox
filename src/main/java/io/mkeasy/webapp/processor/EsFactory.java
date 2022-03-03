package io.mkeasy.webapp.processor;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
// import org.elasticsearch.client.Request;
// import org.elasticsearch.client.Response;
// import org.elasticsearch.client.RestClient;

import io.mkeasy.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;


/*
 * 사용법 : 
 *  
 * @Autowired
 * EsFactory esFactory;
 * 
 * final String method = "GET";
 * final String endpoint = "chickenfarm-broiler-idx/_search";
 *
 * String entity = esFactory.getEsQuery("stat/broiler", "broiler-salesR");
 * JSONObject queryObj = esFactory.renderEsQuery(entity, commandMap);
 * String res = esFactory.performRequest(method, endpoint, queryObj);
 *	
 */

@Slf4j
public class EsFactory {

	@Autowired
	WebUtil webUtil;

	@Autowired
	ServletContext context;

//	public String getEsQuery(String path, String fileName) throws IOException {
//		String queryPath = "/WEB-INF/sql/elastic";
//        queryPath += "/"+path+"/"+fileName+".json";
//		InputStream is = context.getResourceAsStream(queryPath);
//		String jsonQuery = IOUtils.toString(is, "UTF-8"); 
//		return jsonQuery;
//	}
//
//	public JSONObject renderEsQuery(String entity, CommandMap commandMap) {
//		Jinjava jinjava = new Jinjava();
//		String template = jinjava.render(entity, commandMap.getEgovMap());
//		JSONObject renderedObj = new JSONObject(template);
//		return renderedObj;
//	}
//	
//	@Autowired(required = false)
//	RestClient restClient;
//	
//	public String performRequest(String method, String endpoint, JSONObject queryObj) throws ParseException, IOException {
//		Request req = new Request(method, endpoint);
//		req.setJsonEntity(queryObj.toString());
//		Response response = restClient.performRequest(req);
//		String res = (EntityUtils.toString(response.getEntity()));
//		JSONObject object = new JSONObject(res);
//		return object.toString(2);
//	}
//
//	public JSONObject getJSONObject(JSONObject innerObj, String key) {
//		return JSONUtil2.getJSONObject(innerObj, key);
//	}
//
//	public String getValue(JSONObject tmpObj, String key, String defaultValue, boolean roundOk) {
//		return JSONUtil2.getValue(tmpObj, key, defaultValue, roundOk);
//	}

}
