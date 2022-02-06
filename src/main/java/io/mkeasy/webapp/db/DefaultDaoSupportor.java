package io.mkeasy.webapp.db;

import io.mkeasy.webapp.utils.DefaultMapRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DefaultDaoSupportor {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 쿼리를 실행하고 기타 정보를 설정한 후 결과를 반환한다.
     *
     * @param id
     * @param query
     * @param isSingleRow
     * @param params
     * @param resultSet
     * @throws Exception
     */
    public void executeQuery(String id, String query, boolean isSingleRow, CaseInsensitiveMap params, Map<String, Object> resultSet) throws Exception {
        Object result;

        queryLogPrint(query, params);

        long st = System.currentTimeMillis();

        if (StringUtils.startsWithIgnoreCase(query, "select")) {//SELECT쿼리 실행
            List<Map<String, Object>> rows = jdbcTemplate.query(query, new DefaultMapRowMapper(), params);
            //메타데이타 설정
            setMetaData(id, rows, resultSet);
            result = isSingleRow ? makeSingleRow(id, params, rows) : rows;
        } else {//기타 쿼리 실행
            result = new Integer(jdbcTemplate.update(query, params));
        }

        queryExecuteTimePrint(st, System.currentTimeMillis());

        //결과저장
        resultSet.put(id, result);
    }

    /**
     * 단일레코드 반환인 경우 첫번째 레코드 반환하고,
     * 각필드의 값이 다음 쿼리의 인자로 사용될 수 있도록 파라메터에 추가해준다.
     *
     * @param id
     * @param params
     * @param rows
     * @return
     */
    private Map<String, Object> makeSingleRow(String id, CaseInsensitiveMap params, List<Map<String, Object>> rows) {
        if (rows.size() < 1) {
            return new HashMap<String, Object>();
        }

        Map<String, Object> row = rows.get(0);
        //단일 레코드인 경우 결과를 쿼리의 파라메터에 추가해 준다.
        for (String fld : row.keySet()) {
            params.put(id + "." + fld, rows.get(0).get(fld));
        }

        return row;
    }

    /**
     * 메타데이타를 반환한다.
     *
     * @param id
     * @param rows
     * @param resultSet
     */
    @SuppressWarnings("unchecked")
    private void setMetaData(String id, List<Map<String, Object>> rows, Map<String, Object> resultSet) throws Exception {
        ResultSetMetaData rsmd = null;

        if (rows.size() > 0) {
            rsmd = (ResultSetMetaData) rows.get(0).get("_META_DATA_");
            rows.get(0).remove("_META_DATA_");
        }

        if (rsmd == null) {
            return;
        }

        LinkedCaseInsensitiveMap<Object> meta = new LinkedCaseInsensitiveMap<Object>();
        int count = rsmd.getColumnCount() + 1;

        for (int i = 1; i < count; i++) {
            LinkedCaseInsensitiveMap<Object> data = new LinkedCaseInsensitiveMap<Object>();
            String key = rsmd.getColumnLabel(i).toLowerCase();

            data.put("label", key);
            data.put("name", rsmd.getColumnName(i).toLowerCase());
            data.put("type", rsmd.getColumnTypeName(i).toLowerCase());
            data.put("size", rsmd.getColumnDisplaySize(i));
            data.put("precision", rsmd.getPrecision(i));
            data.put("scale", rsmd.getScale(i));

            meta.put(key, data);
        }

        resultSet.put(id + "_meta_", meta);


    }

    private void queryLogPrint(String sql, CaseInsensitiveMap params) {
        String log_line = "\n--------------------------------------------------------------"
                + "--------------------------------------------------------------\n";
        log.info(log_line + paramMarkingValue(sql, params) + log_line);
    }

    private String paramMarkingValue(String sql, CaseInsensitiveMap params) {
        String[] s = sql.split(":");

        for (int i = 1; i < s.length; i++) {
            String[] names = s[i].split("[, ();='\n\r\t/*-+%^|]");
            if (!(names[0].toUpperCase()).equals("MI") && !(names[0].toUpperCase()).equals("SS")) {
                if (params.get(names[0]) != null && !params.get(names[0]).equals("")) {
                    sql = sql.replace(":" + names[0], "'" + params.get(names[0]) + "'");
                }
            }
        }
        return sql;
    }

    private void queryExecuteTimePrint(long st, long et) {
        String log_line = "\n--------------------------------------------------------------"
                + "--------------------------------------------------------------\n";

        log.info(log_line + "-- query execute time : " + (et - st) / 1000);
    }

}
