package io.mkeasy.utils;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) 
	{
		Object key = target.getClass().getSimpleName() + "_"
				+ method.getName() + "_"
				+ StringUtils.arrayToDelimitedString(params, "_");
		log.info("cacheKey : {}", key);
		return key;
	}

}