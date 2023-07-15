package io.mkeasy.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
// import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.SerializationUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapUtil {
	public final static Map EMPTY = Collections.EMPTY_MAP ;
	
	public final static <K, V> Map<K, V> newMap(){
		return new HashMap<K, V>() ;
	}
	
	public final static <K, V> Map<K, V> emptyMap(){
		return Collections.<K, V>emptyMap();
	}

	public final static <K, V> Map<K, V> newSyncMap(){
		return Collections.synchronizedMap(new HashMap<K, V>()) ;
	}


	public static final <V> Map<String, V> newCaseInsensitiveMap() {
		return new CaseInsensitiveMap();
	}

	public final static <K, V> Map<K, V> newOrdereddMap(){
		return new LinkedHashMap<K, V>() ;
	}
	
	public static<K, T> Map<K, T> create(K key, T value) {
		Map<K, T> result = new HashMap<K, T>() ;
		result.put(key, value) ;
		return result;
	}

	// java8 : Map to List
//	public final static <T> List<T> toList(Map<String, ?> obj) {
//		return (List<T>) obj.values().stream().collect(Collectors.toList());
//	}

	public static Map<String, Object> stringMap(String key, Object value) {
		Map<String, Object> result = new HashMap<String, Object>() ;
		result.put(key, value) ;
		return result;
	}	
	

	public static <K, V> Map<K, V> filterMap(Map<K, V> map, String keypattern) {
        try {
            Map<K, V> filtered = map.getClass().newInstance();
            for (Entry<K, V> entry : map.entrySet()) {
                K key = entry.getKey();
                if (key.toString().matches(keypattern)) {
                    filtered.put(key, entry.getValue());
                }
            }
            return filtered;
        } catch (IllegalAccessException iex) {
            throw new IllegalArgumentException(iex) ;
        } catch (InstantiationException iex) {
        	throw new IllegalArgumentException(iex) ;
		}
    }

	public static Map<String, Object> toFlat(Map<String, Object> map) {
		return toFlat(map, '.') ;
	}
	
	public static Map<String, Object> toFlat(Map<String, Object> map, char div) {
		Map<String, Object> result = newMap() ;
		for (Entry<String, Object> entry : map.entrySet()) {
			recursiveObject(result, entry.getKey(), div, entry.getValue()) ;
		}

		return result ;
	}
	
	public static org.json.JSONObject toJSON(Map<String, Object> map) throws Exception {
		// Map의 값 중 null일경우 '' 공백으로 변환 처리 합니다.
		// No converter for [class org.json.JSONObject] with preset Content-Type 'null']
		return JSONUtil.convertMapToJson(map);
	}
	
	private static void recursiveObject(Map<String, Object> parent, String parentPath, char div, Object value){
		if (value instanceof Map){
			Map<String, Object> that = (Map) value ;
			for (Entry<String, Object> entry : that.entrySet()) {
				recursiveObject(parent, parentPath +  div + entry.getKey(), div, entry.getValue()) ;
			}
		} else if (value instanceof List){
			parent.put(parentPath, value) ;
		} else {
			parent.put(parentPath, value) ;
		}
	}
	
	public static void printMap (Map<?, ?> map) {
		if(map==null) return;
		if(!(map instanceof Map)) return;
        map.entrySet()
           .stream()
           .forEach(e -> log.debug(e.getKey() + " = " + e.getValue()));
    }

	public static Optional<Entry<String, Object>> findMap (Map<String, Object> map, String key) {
		if(map == null || key == null) return null;
		if(!(map instanceof Map)) return null;
		return map.entrySet()
				.stream()
				.filter(e -> e.getKey().equalsIgnoreCase(key))
				.findFirst();
	}
	 
	// request.getParameterMap() 복사용
	public static Map<String, String[]> clone(Map<String, String[]> origMap) {
		Set<Entry<String, String[]>> entries = origMap.entrySet();
		HashMap<String, String[]> cloneMap = (HashMap<String, String[]>) entries.stream()
				  .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		return cloneMap;
	}
	
	// VO -> MAP, BeanUtils.describe()는 Map<String, String>으로 형변환이 일어납니다.
	public static Map<String, Object> toMap(Object obj) throws IllegalAccessException {
		Map<String, Object> result = new HashMap<String, Object>() ;
		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			String key = field.getName();
			Object value = field.get(obj);
			result.put(key, value);
		}
    	return result;
	}
	
	
	
}
