package io.mkeasy.webapp.processor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import io.mkeasy.webapp.utils.DefaultLowerCaseMap;
import io.mkeasy.webapp.utils.RSMeta;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

@Slf4j
@Service
public class TableInfoProcessor implements ProcessorService {
	SqlSession sqlSession;

	public Object execute(ProcessorParam processorParam) throws Exception {
		if(sqlSession==null){
			sqlSession = (SqlSession)ProcessorServiceFactory.getBean(SqlSession.class);
		}
		String path = processorParam.getQueryPath();
		CaseInsensitiveMap params = processorParam.getParams();
		String action = processorParam.getAction();
		ServletRequest request = processorParam.getRequest();
		Map<String, Object> resultSet = new LinkedCaseInsensitiveMap<Object>();
		
		String tableName = (String)params.get("table_name");
		
		if(StringUtils.isEmpty(tableName)){
			resultSet.put("list", getTables());
			
		}else{
			resultSet.put("list", getColumns(tableName));
		}
		return resultSet;
	}

	private List<Map<String,Object>> getTables() throws Exception {
		String[] tableTypes = { "TABLE" };
		ResultSet rs = sqlSession.getConnection().getMetaData().getTables(null, null, null, tableTypes);

		return getList(rs);
		
	}
	private List<Map<String,Object>> getColumns(String table) throws Exception {
		
		ResultSet rs = sqlSession.getConnection().getMetaData().getColumns(null, null, table, null);

		return getList(rs);
		
	}
	private List<Map<String,Object>> getList(ResultSet rs) throws Exception {
		List<Map<String,Object>> list = null;
		
		try {
			list = new ArrayList<Map<String,Object>>();
			ResultSetMetaData rsmd = rs.getMetaData();
		
			int cols = rsmd.getColumnCount();
			
			while (rs.next()) {
				Map<String, Object> row = new DefaultLowerCaseMap();
				list.add(row);
				rsmd = rs.getMetaData();
				for (int i = 1; i <= cols; i++) {
					row.put(rsmd.getColumnName(i), rs.getString(i));
				}
			}
			
		} finally{
			rs.close();			
		}
		
		return list;
	}
}
