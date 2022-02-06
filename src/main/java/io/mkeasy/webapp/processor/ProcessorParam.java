package io.mkeasy.webapp.processor;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessorParam {
    private String queryPath;
    private String action;
    private CaseInsensitiveMap params;
    private Map<String, Object> context;
    private ServletRequest request;
    private ServletResponse response;
    private List<String> processorList;
    private Map<String, Object> processorResult;

    public ServletResponse getResponse() {
        return response;
    }

    public void setResponse(ServletResponse response) {
        this.response = response;
    }

    public void setProcessorResult(Map<String, Object> processorResult) {
        this.processorResult = processorResult;
    }

    public List<String> getProcessorList() {
        return processorList;
    }

    public ProcessorParam(String loopId) {
        context = new HashMap<String, Object>();
        processorList = new ArrayList<String>();
    }

    public String getQueryPath() {
        return queryPath;
    }

    public void setQueryPath(String queryPath) {
        this.queryPath = queryPath;
    }

    public CaseInsensitiveMap getParams() {
        return params;
    }

    public void setParams(CaseInsensitiveMap params) {
        this.params = params;
        // ProcessorServiceFactory.setReqParam((HttpServletRequest) request, params);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ServletRequest getRequest() {
        return request;
    }

    public void setRequest(ServletRequest request) {
        this.request = request;
        // ProcessorServiceFactory.setReqParam((HttpServletRequest) request, params);
    }

    public Map<String, Object> getContext() {
        return context;
    }

}
