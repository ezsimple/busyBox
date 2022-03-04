package io.mkeasy.webapp.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MyBatisProcessor implements ProcessorService {

    @Autowired
    private SqlSession sqlSession;

    // private List<Object> EMPTY_LIST = Collections.emptyList();

    public Object execute(ProcessorParam processorParam) throws Exception {
        String nameSpace = processorParam.getNameSpace();
        String nameSpaceId = processorParam.getNameSpaceId();
        CaseInsensitiveMap params = processorParam.getParams();

        String statementId = nameSpace + "." + nameSpaceId;

        Map<String, Object> resultSet = new LinkedCaseInsensitiveMap<Object>();

        log.debug("queryPath = {}, action = {}", nameSpace, nameSpaceId);
        Object result = null;
        SqlCommandType sqlCommandType = getSqlCommandType(nameSpace, nameSpaceId);

        if (sqlCommandType == SqlCommandType.SELECT) {
            result = sqlSession.selectList(statementId, params);
        }

        if (sqlCommandType == SqlCommandType.INSERT) {
            result = sqlSession.insert(statementId, params);
        }

        if (sqlCommandType == SqlCommandType.UPDATE) {
            result = sqlSession.update(statementId, params);
        }

        if (sqlCommandType == SqlCommandType.DELETE) {
            result = sqlSession.delete(statementId, params);
        }

        if (sqlCommandType == SqlCommandType.UNKNOWN) {
            throw new Exception("queryId=" + statementId + " does not exist");
        }

        resultSet.put(statementId, result);
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

    public SqlCommandType getSqlCommandType(String path, String action) {
        String returnId = path + "." + action;
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
