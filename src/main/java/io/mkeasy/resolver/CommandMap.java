package io.mkeasy.resolver;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;

import io.mkeasy.utils.EgovMap;
import io.mkeasy.utils.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommandMap {

	Map<String,Object> parameterMap = new ConcurrentHashMap<String,Object>();

    public Object get(String key){
        return parameterMap.get(key);
    }
    
    // 단일 파라미터의 경우
    // 주의 : trim 이 추가됨
    public String getParam(String key){
    	Object obj = this.get(key);
    	if(obj == null) return "";
    	if(obj instanceof Integer 
    			|| obj instanceof Double
    			|| obj instanceof String) {
    		return StringUtils.trim(String.valueOf(obj));
    	}
        return StringUtils.trim(((String[])obj)[0]);
    }

    public void put(String key, Object value){
    	// ConcurrentHashMap.put() method 
    	// This method throws nullpointerexception : if the specified key or key is null
    	// if value is null, default value is "" empty
        parameterMap.put(key, Objects.isNull(value)?"":value);
    }

    public Object remove(String key){
        return parameterMap.remove(key);
    }

    public boolean containsKey(String key){
        return parameterMap.containsKey(key);
    }

    public boolean containsValue(Object value){
        return parameterMap.containsValue(value);
    }

    public void clear(){
        parameterMap.clear();
    }

    public Set<Entry<String, Object>> entrySet(){
        return parameterMap.entrySet();
    }

    public Set<String> keySet(){
        return parameterMap.keySet();
    }

    public boolean isEmpty(){
        return parameterMap.isEmpty();
    }

    public void putAll(Map<? extends String, ?extends Object> m){
        parameterMap.putAll(m);
    }

    public Map<String,Object> getMap(){
        return parameterMap;
    }
    
    public CaseInsensitiveMap getQueryMap() {
		String key = null;
		String[] values = null;
		CaseInsensitiveMap map = new CaseInsensitiveMap();
    	for(Entry<String, Object> entry : this.getMap().entrySet()) {
    		key = entry.getKey();
    		Object o = entry.getValue();
    		if(o instanceof String) {
                map.put(key, o);
    		}
    		if(o instanceof String[]) {
                values = (String[]) entry.getValue();
                map.put(key, (values.length>1?values:values[0]));
    		}
    	}
    	return map;
    }
    
    // for CamelCase
    public EgovMap getEgovMap() {
		String key = null;
		String[] values = null;
		EgovMap map = new EgovMap();
    	for(Entry<String, Object> entry : this.getMap().entrySet()) {
    		key = entry.getKey();
    		Object o = entry.getValue();
    		if(o instanceof String) {
                map.put(key, o);
    		}
    		if(o instanceof String[]) {
                values = (String[]) entry.getValue();
                map.put(key, (values.length>1?values:values[0]));
    		}
    	}
    	return map;
    }
    
    public void debugParams() {
//    	for(Entry<String, Object> entry : this.getMap().entrySet()) {
//    		log.debug("{}:{}",entry.getKey(), entry.getValue());
//    	}
    	// Class<?> caller = sun.reflect.Reflection.getCallerClass(); // java.lang.InternalError: CallerSensitive annotation expected at frame 1
    	Class<?> source = org.slf4j.helpers.Util.getCallingClass();
    	StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
    	StackTraceElement e = stacktrace[2];//maybe this number needs to be corrected
    	log.debug("{}.{}:{}, params => {}" ,
    			source.getSimpleName(), // className
    			this.getMethod(), 		// e.getMethodName()
    			e.getLineNumber(), 		// sourceLineNumber
    			toString());			// parameters
    }
    
    // for CacheKeyGenerator
    public String toString() {
		return JSONUtil.convertMapToJson(this.getMap()).toString();
    }

	String method = null;
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMethod() {
		return method;
	}
    
}