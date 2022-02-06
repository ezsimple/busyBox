package io.mkeasy.webapp.processor;

import io.mkeasy.utils.ListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyBatisProcessor implements ProcessorService {
    private Map<String, List<MappedStatementInfo>> mappedStatementInfoMap = null;
    private SqlSession sqlSession;

    private List<Object> EMPTY_LIST = Collections.emptyList();

    public Object execute(ProcessorParam processorParam) throws Exception {
        String path = processorParam.getQueryPath();
        CaseInsensitiveMap params = processorParam.getParams();

        String queryPath = processorParam.getQueryPath();
        String action = processorParam.getAction();
        String returnId = queryPath + "." + action;

        Map<String, Object> resultSet = new LinkedCaseInsensitiveMap<Object>();

        log.debug("queryPath = {}, action = {}", queryPath, action);
        Object result = null;
        SqlCommandType sqlCommandType = getSqlCommandType(path, action);

        if (sqlCommandType == SqlCommandType.SELECT) {
            result = sqlSession.selectList(returnId, params);
            if (result.equals(EMPTY_LIST)) { // 한개의 ROW가 모두 NULL일 경우
                result = ListUtil.EMPTY_LIST;
            }
        }

        if (sqlCommandType == SqlCommandType.INSERT) {
            result = sqlSession.insert(returnId, params);
        }

        if (sqlCommandType == SqlCommandType.UPDATE) {
            result = sqlSession.update(returnId, params);
        }

        if (sqlCommandType == SqlCommandType.DELETE) {
            result = sqlSession.delete(returnId, params);
        }

        if (sqlCommandType == SqlCommandType.UNKNOWN) {
            throw new Exception("queryId=" + returnId + " does not exist");
        }

        resultSet.put(returnId, result);
        return resultSet;

    }

    public Object execute(String ns, String nsId, List list) throws Exception {

        final String returnId = ns + "." + nsId;
        SqlCommandType sqlCommandType = getSqlCommandType(ns, nsId);

        if (sqlCommandType == SqlCommandType.SELECT) {
            return sqlSession.selectList(returnId, list);
        }

        if (sqlCommandType == SqlCommandType.INSERT) {
            return sqlSession.insert(returnId, list);
        }

        if (sqlCommandType == SqlCommandType.UPDATE) {
            return sqlSession.update(returnId, list);
        }

        if (sqlCommandType == SqlCommandType.DELETE) {
            return sqlSession.delete(returnId, list);
        }

        if (sqlCommandType == SqlCommandType.UNKNOWN) {
            throw new Exception("queryId=" + returnId + " does not exist");
        }

        throw new Exception(sqlCommandType + " does not exist");
    }

    class MappedStatementInfo {
        boolean isSingleRow = false;
        String id;
        String returnId;
        boolean isSelect = false;

        public MappedStatementInfo(String id, String returnId, boolean isSelect, boolean isSingleRow) {
            this.isSingleRow = isSingleRow;
            this.id = id;
            this.returnId = returnId;
            this.isSelect = isSelect;
        }
    }

    public List<MappedStatementInfo> getList(String path, String action) {
        String key = path + "." + action;

        if (mappedStatementInfoMap != null) {
            List<MappedStatementInfo> result = mappedStatementInfoMap.get(key);
            if (result != null)
                return result;
        }

        if (sqlSession == null) {
            sqlSession = (SqlSession) ProcessorServiceFactory.getBean(SqlSessionTemplate.class);
        }

        List<String> idList = new ArrayList<String>();
        Map<String, List<MappedStatementInfo>> msInfoMap = new LinkedCaseInsensitiveMap<List<MappedStatementInfo>>();

        Collection<String> collection = sqlSession.getConfiguration().getMappedStatementNames();

        for (String id : collection) {
            if (!StringUtils.contains(id, ".")) continue;
            idList.add(id);
        }

        String[] ids = idList.toArray(new String[0]);
        Arrays.sort(ids);

        for (String id : ids) {
            boolean isSingleRow = false;
            boolean isSelect = false;
            String keyId = id;
            String returnId = id;
            MappedStatement mappedStatement = null;

            try {
                mappedStatement = sqlSession.getConfiguration().getMappedStatement(id);
            } catch (Exception e) {
                continue;
            }

            if (!id.equals(mappedStatement.getId())) {
                continue;
            }

            //mappedStatement.getConfiguration().
            if (mappedStatement.getSqlCommandType() == SqlCommandType.SELECT) {
                isSelect = true;
            }

            MappedStatementInfo msi = new MappedStatementInfo(id, returnId, isSelect, isSingleRow);

            List<MappedStatementInfo> list = msInfoMap.get(keyId);

            if (list == null) {
                list = new ArrayList<MyBatisProcessor.MappedStatementInfo>();
                msInfoMap.put(keyId, list);
            }
            list.add(msi);
        }

        mappedStatementInfoMap = msInfoMap;
        ProcessorServiceFactory.setMyBatisMappedStatementInfoMap(mappedStatementInfoMap);

        return mappedStatementInfoMap.get(key);
    }

    public SqlCommandType getSqlCommandType(String path, String action) {
        String returnId = path + "." + action;

        if (sqlSession == null) {
            sqlSession = (SqlSession) ProcessorServiceFactory.getBean(SqlSessionTemplate.class);
        }

        Collection<String> collection = sqlSession.getConfiguration().getMappedStatementNames();
        if (!collection.contains(returnId)) {
            log.error("returnId = {} does not exist in MappedStatementNames", returnId);
            return SqlCommandType.UNKNOWN;
        }

        MappedStatement mappedStatement = null;
        try {
            mappedStatement = sqlSession.getConfiguration().getMappedStatement(returnId);
        } catch (Exception e) {
        }

        return mappedStatement.getSqlCommandType();
    }
}
