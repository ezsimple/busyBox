package io.mkeasy.webapp.utils;

import org.apache.commons.lang.StringUtils;


/**
 * 이클래스는 쿼리에서 사용되는 EL문법에서 사용할 함수입니다.
 * 함수는 public static으로 작성하고 오버로딩은 지원하지 않습니다.
 * 
 * @author shsuk
 *
 */
public class ELFunctions {

	public static int indexOf(String str, String searchStr){
		return StringUtils.indexOf(str, searchStr);
	}


}
