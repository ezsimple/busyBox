package io.mkeasy.utils;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import io.mkeasy.utils.ListUtil;
import io.mkeasy.utils.ResultUtil;

@Slf4j
public class QueryUtil {
	
	public static Map<String, Object> getResult(List<Map<String, Object>> list) throws Exception {
		Map<String, Object> resultMap = ResultUtil.newResult();
		ResultUtil.init(resultMap);

		if (list == null) {
			ResultUtil.setMessage(resultMap, "LIST IS NULL");
			return resultMap;
		}

		if (list.isEmpty()) {
			resultMap.put("list", ListUtil.EMPTY);
			resultMap.put("total", 0);
			log.debug("row : {}, total : {}", list, 0);
			ResultUtil.setSuccessMessage(resultMap, "LIST IS EMPTY");
			return resultMap;
		}

		resultMap.put("list", list);
		resultMap.put("total", list.size());
		ResultUtil.setSuccessMessage(resultMap, "SUCCESS");

		return resultMap;
	}

	// ======================================================================
	// MSSQL : SQL Server를 사용할 경우 페이징 쿼리에 대응
	// ======================================================================
	//	SELECT COUNT(*) OVER() AS TOTAL, 
	//		T1.USERNAME, T1.NAME, T2.EMAIL, T3.AUTHORITY_NAME, T2.MODDATE 
	//	FROM 
	//		USER_TBLC T1, USER_DTL_TBLC T2, AUTHORITY_TBLC T3
	//	WHERE 
	//		1=1
	//		AND T1.USERNAME = T2.USERNAME
	//		AND T1.USERNAME = T3.USERNAME
	//	ORDER BY T1.USERNAME 
	//		OFFSET 0  ROWS
	//	FETCH NEXT 10 ROWS ONLY	
	// ======================================================================
	public static Map<String, Object> getPagingResult(List<Map<String, Object>> list) throws Exception {
		Map<String, Object> resultMap = ResultUtil.newResult();
		ResultUtil.init(resultMap);

		if (list == null) {
			ResultUtil.setMessage(resultMap, "LIST IS NULL");
			return resultMap;
		}

		if (list.isEmpty()) {
			resultMap.put("list", ListUtil.EMPTY);
			resultMap.put("total", 0);
			log.debug("row : {}, total : {}", list, 0);
			ResultUtil.setSuccessMessage(resultMap, "LIST IS EMPTY");
			return resultMap;
		}

		Map<String, Object> row = list.get(0);
		log.debug("row : {}", row);

		// MyBatis의 쿼리문(Xml)에 resultType="hashmap" 대신, resultType="net.ion.util.LowerKeyMap" 을 사용해야 한다"
		Object total = row.get("total"); 
		log.debug("LowerKeyMap total : {}", total); //

		resultMap.put("list", list);
		resultMap.put("total", total==null?"0":total);
		ResultUtil.setSuccessMessage(resultMap, "SUCCESS");

		return resultMap;
	}

	// (중요) result parameter의 값은 getResult의 결과값이다.
	public static List<Object> getList(Map<String, Object> result) {
		if (result == null)
			return ListUtil.EMPTY;
		return (List) result.get("list");
	}

	// (중요) result parameter의 값은 getResult의 결과값이다.
	public static List<Object> find(Map<String, Object> result, String fieldName) {
		if (result == null)
			return ListUtil.EMPTY;
		List list = ListUtil.newList();
		for (Object obj : (List) result.get("list")) {
			if (obj == null)
				continue;
			list.add(((Map) obj).get(fieldName));
		}
		return list;
	}

}
