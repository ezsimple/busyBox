package io.mkeasy.utils;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Class 내용 : Properties Util 관련
 * @FileName : PropertiesUtil.java
 * @author : 이민호 
 * @since : 2019.12.06.
 * @version 1.0
 *
 * 1. applicationContext.xml 에서 util:properties 사용합니다.
 * 2. #{systemProperties['service.mode']}의 값은 톰캣 VM Argument에서 -Dservice.mode=local 로 선언되어 있습니다.
 *    또한 여러개의 constant를 설정할 수 있습니다.
 * <util:properties id="constant" location="/WEB-INF/config/properties/#{systemProperties['service.mode']}/constantDef.properties"/>
 * 3. @Resource(name="아이디명")을 사용하여, Injection 시킵니다.
 * 아이디명에 따라 여러개의 properties 를 사용할 수 있습니다.
 * 
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *
 *  </pre>
 */


@Slf4j
@Component
public class PropertiesUtil{
	
//	@Resource(name="constant")
//	Properties constant; // do not support static 
//	
//    public Properties getConstant() {
//    	return this.constant;
//    }
//    
//    public String getConstantProperty(String key) {
//    	return getConstant().getProperty(key);
//    }
}