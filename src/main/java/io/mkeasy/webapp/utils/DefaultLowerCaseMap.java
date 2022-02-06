package io.mkeasy.webapp.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.util.LinkedCaseInsensitiveMap;

public class DefaultLowerCaseMap<K,V> extends LinkedCaseInsensitiveMap implements Map{

	private static final long serialVersionUID = 2903927153277211682L;

	/**
	 * 데이타를 가공하여 반환한다.
	 * 데이타가 문자열인 경우 key값이 대문자면 원본 값을 반환하고 아닌 경우는 xml escape문자로 반환한다.
	 * @ 문자 뒤에 포맷을 입력하면 포맷처리하여 반환한다.
	 */
    @SuppressWarnings("unchecked")
	public V get(Object key) {
    	key = key==null ? "" : key;
    	String[] keys = ((String) key).split("@");
    	String newKey = keys[0].toLowerCase();
		Object val = super.get(newKey);
		
		if (val instanceof String) {//데이타가 문자열인 경우
	    	String upperKey = newKey.toUpperCase();
			if(upperKey.equals(keys[0])) {//대문자로 요청한 경우 원본 데이타 리턴

				String newVal = (String) val;
				
				try {
					if(keys.length>1){
						
						if("***".equalsIgnoreCase(keys[1])){//복호화 처리
							if(keys.length>2){//마킹 처리
								return (V) Function.security(newVal, keys[2]);								
							}else{//복호화 처리 TODO
								
								return (V) newVal;
							}
						}else{
							if(keys.length>2){
								Function.masking(newVal, keys[1]);
							}else{
								Function.format(newVal, keys[1]);
							}
						}
					}
					
				} catch (Exception e) {;}
				
				int p = ((String) val).indexOf("<");
				if(p>-1){
					
					newVal = newVal.toLowerCase();
					
					if(newVal.indexOf("<script")>-1){//스크립트가 있는 경우는 강제 인코딩한다.
						// return (V) Function.escapeXml((String)val);
						return (V) ((String)val);
					}
					
				}

				return (V) val;
			}else{//인코딩한다.
				// return (V) Function.escapeXml((String)val);
				return (V) ((String)val);
			}
		}else if(keys.length>1){//포맷을 지정한 경우
			if (val instanceof Date){
				SimpleDateFormat dateFormater = new SimpleDateFormat(keys[1]);
				return (V) dateFormater.format(val);
			}else if (val instanceof Number){
				NumberFormat formatter = new DecimalFormat(keys[1]);
				return (V) formatter.format(val);
			}
		}
		//문자도 아니고 포맷지정도 안한 경우
    	return (V) val;
    }
    public Object remove(Object key) {
        return super.remove(String.valueOf(key).toLowerCase());
    }
    @SuppressWarnings("unchecked")
	public Object put(Object key, Object value) {
		
		if (value instanceof Date) {
			Date nVal = (Date) value;
			Timestamp ts = new Timestamp(nVal.getTime());
			value =  ts;
		}
    	return super.put(String.valueOf(key).toLowerCase(), value);
    }

    public boolean containsKey(Object key) {
        if(key instanceof String){
            key = ((String) key).toLowerCase() ;
        }
        return super.containsKey(key) ;
    }
}
