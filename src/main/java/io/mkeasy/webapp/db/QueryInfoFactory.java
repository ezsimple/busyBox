package io.mkeasy.webapp.db;

import io.mkeasy.webapp.processor.ProcessorServiceFactory;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryInfoFactory {
    protected static Map<String, Map<String, JSONObject>> queryInfoMap;
    protected static Map<String, Long> queryChangeMap;
    protected static String root = "";

    /**
     * 쿼리파일에 있는 쿼리를 가져온다.
     * @param path
     * @return
     * @throws Exception
     */
    public static Map<String, JSONObject> findQuerys(String path) throws Exception {
        if (queryInfoMap == null) {
            queryInfoMap = new ListOrderedMap();
            queryChangeMap = new ListOrderedMap();
            root = ProcessorServiceFactory.getQueryFullPath();
        }

        File f = new File(root, path + ".sql");

        Map<String, JSONObject> queryInfos = queryInfoMap.get(path);

        if (queryInfos != null) {
            long time = queryChangeMap.get(path);

            if (time == f.lastModified()) {
                return queryInfos;
            }
        }

        if (!f.exists()) {
            return queryInfos;
        }

        queryInfos = new ListOrderedMap();

        //쿼리를 로딩하여 저장한 후 리턴 한다.
        int idx = 1;
        StringBuffer sb = new StringBuffer();

        List<String> queryList = FileUtils.readLines(f, "utf-8");

        for (String line : queryList) {
            sb.append(StringUtils.substringBefore(line, "//")).append('\n');
        }

        String[] querys = sb.toString().split(";");

        for (String query : querys) {
            String infoStr = "{}";
            JSONObject info = null;
            query = query.trim();

            if (query.startsWith("/*")) {
                infoStr = StringUtils.substringBetween(query, "/*", "*/");
                query = StringUtils.substringAfter(query, "*/").trim();
            }

            if (StringUtils.isEmpty(query)) {
                continue;
            }

            info = JSONObject.fromObject(infoStr);

            String id = (String) info.get("id");
            if (id == null) {
                id = "_rows" + idx;
                idx++;
            }

            info.put("id", id);
            info.put("query", query);
            String action = (String) info.get("action");
            //아이디가 중복되면 중복을 피하기 위해 action을 키로 추가한다.
            //키는 중복을 피하기 위한 용도로만 사용하고 다른 곳에서는 서브쿼리를 찾기 위한 용도 외에 사용되지 않는다.
            if (queryInfos.containsKey(id)) {

                id += "_" + action;
            }
            ;
            //action이 있는 경우 액션을 맵에 넣어 다시 저장한다.
            if (StringUtils.isNotEmpty(action)) {
                String[] actions = action.split(",");
                Map<String, String> actMap = new HashMap<String, String>();
                for (String key : actions) {
                    actMap.put(key.trim(), action);
                }
                info.put("action", actMap);
            }
            queryInfos.put(id, info);
        }

        queryInfoMap.put(path, queryInfos);
        queryChangeMap.put(path, f.lastModified());

        return queryInfos;
    }

    /**
     * 서브쿼리를 찾아 완전한 쿼리로 만들어 준다.
     *
     * @param queryId
     * @param query
     * @param queryInfos
     * @return
     * @throws Exception
     */
    public static String makeQuery(String queryId, String query, Map<String, JSONObject> queryInfos) throws Exception {
        String[] subQueryIds = StringUtils.substringsBetween(query, "@{", "}");

        if (subQueryIds == null) {
            return query;
        }

        for (String subQueryId : subQueryIds) {
            JSONObject queryInfo = queryInfos.get(subQueryId);
            if (queryInfo == null) {
                throw new RuntimeException(queryId + "쿼리에서 사용하는 서브쿼리 " + subQueryId + "가 존재하지 않습니다.");
            }
            String subQuery = queryInfo.getString("query");
            query = StringUtils.replace(query, "@{" + subQueryId + "}", subQuery);
        }

        return query;
    }

    public static boolean getBoolean(String key, JSONObject queryInfo, boolean defaultValue) throws Exception {
        return queryInfo.containsKey(key) ? queryInfo.getBoolean(key) : defaultValue;
    }

    public static String getString(String key, JSONObject queryInfo, String defaultValue) throws Exception {
        Object val = queryInfo.get(key);
        return val == null ? "" : val.toString();
    }

}
