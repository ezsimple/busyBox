package io.mkeasy.webapp.processor;

import io.mkeasy.webapp.db.DefaultDaoSupportor;
import io.mkeasy.webapp.processor.MyBatisProcessor.MappedStatementInfo;
import io.mkeasy.webapp.utils.CacheService;
import io.mkeasy.webapp.utils.RSMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProcessorServiceFactory implements ApplicationContextAware {
    private static Map<String, ProcessorService> processorServiceMap = new LinkedCaseInsensitiveMap<ProcessorService>();
    private static Map<String, DefaultDaoSupportor> daoSupportorMap = new HashMap<String, DefaultDaoSupportor>();
    private static Map<String, Map<String, RSMeta>> rsMeta = new HashMap<String, Map<String, RSMeta>>();
    private static ApplicationContext applicationContext;
    private static String queryFullPath = null;
    private static String repositoryPath = null;
    private static String defaultDataSourceName = null;
    private static CacheService cacheService = null;
    private static Map<String, List<MappedStatementInfo>> myBatisMappedStatementInfoMap = null;
    private static Map<String, String> fieldCash = new HashMap<String, String>();

    public static void setMyBatisMappedStatementInfoMap(Map<String, List<MappedStatementInfo>> myBatisMappedStatementInfoMap) {
        ProcessorServiceFactory.myBatisMappedStatementInfoMap = myBatisMappedStatementInfoMap;
    }

    public static String getQueryFullPath() {
        return queryFullPath;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        ProcessorServiceFactory.applicationContext = applicationContext;
        //프로세서 서비스를 초기화 한다.
        // init();
    }

    /**
     * 프로세서 서비스를 초기화 한다.
     */
//    public static void init() {
//
//        cacheService = new CacheService();
//        cacheService.start();
//
//        if (processorServiceMap.size() > 0) {
//            //return;
//        }
//        String[] serviceList = applicationContext.getBeanNamesForType(ProcessorService.class);
//
//        for (String key : serviceList) {
//            String name = "";
//            try {
//                ProcessorService autoProcessor = (ProcessorService) applicationContext.getBean(key);
//                name = autoProcessor.toString();
//
//                name = StringUtils.substringBetween(name, "io.mkeasy.webapp.processor.", "Processor").toLowerCase();
//                name = name.replace('.', '_');
//                processorServiceMap.put(name, autoProcessor);
//
//            } catch (Exception e) {
//                log.error(name + "의 이름이 Processor 로 끝나지 않아 등록되지 않았습니다.");
//            }
//        }
//
//        serviceList = applicationContext.getBeanNamesForType(DataSource.class);
//
//        for (String key : serviceList) {
//            DataSource dataSource = (DataSource) applicationContext.getBean(key);
//
//            DefaultDaoSupportor defaultDaoSupportor = new DefaultDaoSupportor();
//            defaultDaoSupportor.setDataSource(dataSource);
//
//            daoSupportorMap.put(key, defaultDaoSupportor);
//        }
//
//        if (StringUtils.isEmpty(defaultDataSourceName) && serviceList.length > 0) {
//            defaultDataSourceName = serviceList[0];
//        }
//    }
    public static Object getBean(Class cls) {
        String[] idList = applicationContext.getBeanNamesForType(cls);
        if (idList.length == 0) return null;
        return applicationContext.getBean(idList[0]);
    }

    public static ProcessorService getProcessorService(String method) {
        return processorServiceMap.get(method);
    }

    public static DefaultDaoSupportor getDaoSupportor(String dataSourceName) {
        dataSourceName = StringUtils.isEmpty(dataSourceName) ? defaultDataSourceName : dataSourceName;
        return daoSupportorMap.get(dataSourceName);
    }

//    public static CaseInsensitiveMap setReqParam(HttpServletRequest request, CaseInsensitiveMap params, String loopId) {
//        if (request == null || params == null) {
//            return params;
//        }
//
//        Map<String, String[]> parameterMap = request.getParameterMap();
//
//        if (loopId == null) {
//            loopId = "";
//            for (String key : parameterMap.keySet()) {
//                String[] vals = parameterMap.get(key);
//
//                if (vals.length > 1) {
//
//                    if (loopId.compareToIgnoreCase(key) < 0) {
//                        loopId = key;
//                    }
//
//                }
//            }
//        }
//
//        String[] loopValue = parameterMap.get(loopId);
//        List<Map<String, String>> loopList = new ArrayList<Map<String, String>>();
//        if (loopValue != null) {
//            for (int i = 0; i < loopValue.length; i++) {
//                loopList.add(new HashMap<String, String>());
//            }
//        }
//
//        //request정보를 맵에 추가한다.
//        for (String key : parameterMap.keySet()) {
//            String[] vals = parameterMap.get(key);
//            params.put(key, vals[0]);
//            params.put(key + "_", vals);
//            String allVal = "";
//            for (int i = 0; i < vals.length; i++) {
//                String val = vals[i];
//                params.put(key + "[" + i + "]", val);
//                allVal += "," + val;
//            }
//
//            allVal = allVal.length() > 0 ? allVal.substring(1) : allVal;
//            params.put(key + "[]", allVal);
//            params.put(key + "_all", allVal);
//
//            if (loopValue == null) {
//                continue;
//            }
//
//            for (int i = 0; i < loopValue.length; i++) {
//                Map<String, String> map = loopList.get(i);
//                if (i < vals.length) {
//                    map.put(key, vals[i]);
//                }
//            }
//        }
//
//        if (StringUtils.isNotEmpty(loopId)) {
//            params.put("loop_", loopList);
//        } else {
//            params.put("loop_", "");
//        }
//
//        CaseInsensitiveMap sessionMap = new CaseInsensitiveMap();
//        Map<String, Object> session = (Map<String, Object>) request.getAttribute("session");
//        if (session != null) {
//            for (String key : session.keySet()) {
//                Object val = session.get(key);
//
//                if (!(val instanceof String)) {
//                    continue;
//                }
//                sessionMap.put(key, val);
//            }
//
//            params.put("session", sessionMap);
//
//        }
//        params.put("servletPath", request.getServletPath());
//
//        return params;
//    }

    public static Object executeQuery(String ns, String nsId, CaseInsensitiveMap params) throws Exception {
        ProcessorParam processorParam = new ProcessorParam(null);
        processorParam.setQueryPath(ns);
        processorParam.setAction(nsId);
        processorParam.setParams(params);
        //processorParam.setProcessorList(processorList);
        Object obj = ProcessorServiceFactory.getProcessorService("mybatis").execute(processorParam);

        return obj;
    }

}
