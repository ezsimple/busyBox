package io.mkeasy.webapp.processor;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import com.hubspot.jinjava.Jinjava;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import io.mkeasy.resolver.CommandMap;
import io.mkeasy.utils.JSONUtil2;
import io.mkeasy.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MongoFactory {

	@Autowired
	WebUtil webUtil;

	@Autowired
	ServletContext context;

	public String getMongoQuery(String path, String fileName) throws IOException {
		String queryPath = "/WEB-INF/sql/mongo";
        queryPath += "/"+path+"/"+fileName+".json";
		InputStream is = context.getResourceAsStream(queryPath);
		String jsonQuery = IOUtils.toString(is, "UTF-8"); 
		return jsonQuery;
	}

	public JSONObject renderMongoQuery(String entity, CommandMap commandMap) {
		Jinjava jinjava = new Jinjava();
		String template = jinjava.render(entity, commandMap.getEgovMap());
		JSONObject renderedObj = new JSONObject(template);
		return renderedObj;
	}

	@Autowired(required = false)
	MongoTemplate mongoTemplate;
	
	public void executeQuery(String collectionName, JSONObject queryObj, DocumentCallbackHandler dch) {
//		BasicQuery query = new BasicQuery(queryObj.toString());
//		mongoTemplate.executeQuery(query, collectionName, dch);
		
		BasicDBList andList = new BasicDBList();
		andList.add(new BasicDBObject("by", "tutorials point"));
		andList.add(new BasicDBObject("title", "MongoDB Overview")); 
		BasicDBObject and = new BasicDBObject("$and", andList);
		BasicDBObject command = new BasicDBObject("find", "collectionName");
		command.append("filter", and); 
		
		log.debug("{}", command.toJson());

		// mongoTemplate.executeCommand(command.toJson());
	}

	public JSONObject getJSONObject(JSONObject innerObj, String key) {
		return JSONUtil2.getJSONObject(innerObj, key);
	}

	public String getValue(JSONObject tmpObj, String key, String defaultValue, boolean roundOk) {
		return JSONUtil2.getValue(tmpObj, key, defaultValue, roundOk);
	}

}
