package io.mkeasy.webapp.processor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.mkeasy.utils.JSONUtil;
import io.mkeasy.utils.ListUtil;
import io.mkeasy.utils.MapUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryFactory {

	// Non Transaction Query
	public static Object execute(String ns, String nsId, CaseInsensitiveMap params) throws Exception {
		checkNS(ns, nsId);
		return ProcessorServiceFactory.executeQuery(ns, nsId, params);
	}

	// Transaction Query
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public static Object executeTx(String ns, String nsId, CaseInsensitiveMap params) throws Exception {
		checkNS(ns, nsId);
		return ProcessorServiceFactory.executeQuery(ns, nsId, params);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object executeAll(String ns, String nsId, List list) throws Exception {
		checkNS(ns, nsId);
		if(list==null)
			throw new Exception("List parameters is null");
		return executeBulk(ns, nsId, list);
	}

	// Get Query Results using CRUD query result
	// private final static List<Object> EMPTY_LIST = Arrays.asList(new Object[]{ null });
	private final static List<Object> EMPTY_LIST = Collections.emptyList();

	public static Object getResult(final String ns, final String nsId, final Object result) throws Exception {
		checkNS(ns, nsId);
		String key = ns+"."+nsId;
		Object map = ((Map<String, Object>) result).get(key);
		if(map!=null && map instanceof List && map.equals(EMPTY_LIST)) // 한개의 ROW가 모두 NULL일 경우
			return ListUtil.EMPTY;
		return map;
	}

	// result는 getResult(ns,nsId,result)의 값입니다.
	// result에서 List를 가져옵니다.
	public static List<Map<String, Object>> toList(Object result) {
		return (List<Map<String, Object>>) result;
	}

	// result는 getResult(ns,nsId,result)의 값입니다.
	// result에서 첫번째만 Map으로 가져옵니다.
	public static Map<String, Object> toMap(Object result) {
		if(ListUtil.isEmpty((List<Map<String, Object>>) result))
			return MapUtil.EMPTY;
		return MapUtil.toFlat(((List<Map<String, Object>>) result).get(0));
	}

	// getResult(ns,nsId,result)의 값을 key명으로 HashMap에 담아 드립니다.
	public static Map<String, Object> toMap(String key, Object result) {
		if(StringUtils.isEmpty(key) || result == null) return MapUtil.EMPTY;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(key, result);
		return resultMap;
	}
	
	// result는 getResult(ns,nsId,result)의 값입니다.
	// result에서 첫번째 Integer 값만 가져옵니다.
	public static Integer toInt(Object result) {
		if(result instanceof Integer)
			return (Integer) result;
		Object[] obj = ListUtil.toArray(result);
		return (Integer) obj[0];
	}
	
	// result는 getResult(ns,nsId,result)의 값입니다.
	// result에서 첫번째 String 값만 가져옵니다.
	public static String toString(Object result) {
		if (result instanceof String)
			return String.valueOf(result);
		Object[] obj = ListUtil.toArray(result);
		if(obj.length==0) return "";
		return String.valueOf(obj[0]);
	}

	// result는 getResult(ns,nsId,result)의 값입니다.
	// result에서 JSONArray로 변환시켜 줍니다.
	// org.json.simple(google), net.sf.json.JSONObject에서는 
	// DATE, TIMESTAMP 값에 대해 잘못된 파싱을 합니다.
	public static org.json.JSONArray toJSONArray(Object result) {
		return JSONUtil.convertListToJson((List<Map<String, Object>>) result);
	}

	private static void checkNS(String ns, String nsId) throws Exception {
		if (StringUtils.isEmpty(ns)) 
			throw new Exception("nameSpace is empty");
		if (StringUtils.isEmpty(nsId))
			throw new Exception("nameSpaceId is empty");
	}
	
	@Autowired
	MyBatisProcessor myBatisProcessor;

	private Object executeBulk(String ns, String nsId, List list) throws Exception {
		return myBatisProcessor.execute(ns, nsId, list);
	}

}
