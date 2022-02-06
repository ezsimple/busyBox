package io.mkeasy.webapp.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheService extends Thread {
//	Map<String, CacheInfo> cacheMap = new HashMap<String, CacheService.CacheInfo>();
//	
//	class CacheInfo{
//		long  expire;
//		Object data;
//		public CacheInfo(Object data, int sec){
//			this. expire = new Date().getTime() + (sec * 1000);
//			this.data = data;
//		}
//	}
//	
//	public Object get(String key)  {
//
//		CacheInfo cacheInfo = cacheMap.get(key);
//		if(cacheInfo==null){
//			return null;
//		}
//		
//		if(cacheInfo.expire < (new Date().getTime())){
//			cacheMap.remove(key);
//			return null;
//		}
//		
//		return cacheInfo.data;
//	}
//	
//	public void put(String key, Object data) {
//		put(key, data, 60);
//	}
//	
//	public void put(String key, Object data, int sec) {
//		CacheInfo cacheInfo = new CacheInfo(data, sec);
//		cacheMap.put(key, cacheInfo);
//	}
//	public void run() {
//		try {
//			while(true){
//				try {
//					for(String key :cacheMap.keySet()){
//						get(key);
//					}					
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				Thread.sleep(600000);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//
}