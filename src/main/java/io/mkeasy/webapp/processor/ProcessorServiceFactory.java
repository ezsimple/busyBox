package io.mkeasy.webapp.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Slf4j
public class ProcessorServiceFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private MyBatisProcessor myBatisProcessor;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object executeQuery(String ns, String nsId, CaseInsensitiveMap params) throws Exception {
        ProcessorParam processorParam = ProcessorParam.builder()
                .nameSpace(ns)
                .nameSpaceId(nsId)
                .params(params)
                .build();
        Object obj = myBatisProcessor.execute(processorParam);
        return obj;
    }

}
