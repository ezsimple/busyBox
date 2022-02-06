package io.mkeasy.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {
	public static String trim(String str) {
		return StringUtils.trim(str);
	}
	
	public boolean isEmptyArray(String [] array){
		return ArrayUtils.isEmpty(array);
	}	
	
	public boolean isEmpty(String[] array) {
		return ListUtil.isEmpty(array);
	}

	public static String escape(String str) {
		return StringEscapeUtils.escapeXml(str);
	}

	public static String unescape(String str) {
		return StringEscapeUtils.unescapeXml(str);
	}
	
	// null => ""
	public static String trimToEmpty(Object obj) {
		if(ObjectUtils.isEmpty(obj))
			return "";

		String str = String.valueOf(obj);
		if(StringUtils.isEmpty(str))
			return "";

		str = StringUtils.trimToEmpty(String.valueOf(obj));
		if(StringUtils.isEmpty(str)) 
			return "";

		return str;
	}
	
	// 디버깅용
	// Obect 객체를, String 형태로 출력해 준다.
	// 예 : l2u.cmm.usr.dom.User@759bc41f 형태를 
	// ==> 이렇게 표시해준다. l2u.cmm.usr.dom.User@759bc41f[username=devel1,pasㅏsword=$2a$10$s2eqytAKIW/xen4ZmoaoVerzQjpNzldcPrgk0bwq5b3JcacsU7tK6,name=개발자1,isAccountNonExpired=true,isAccountNonLocked=true,isCredentialsNonExpired=true,isEnabled=true,authorities=<null>,email=devel1@link2us.co.kr,phone=00000000001,regdate=Sun Nov 18 20:31:17 EST 2018,asgndate=Sun Nov 18 20:31:17 EST 2018,moddate=Sun Nov 18 20:31:17 EST 2018,pchgdate=Sun Nov 18 20:21:17 EST 2018,isDel=false]
	public static String toString(Object object) {
		return ObjectUtil.toString(object);
	}
	
	// 첫글자만 대문자로 
	public static String toCapitalize(String s){
        if (s==null) return "";
        return String.format("%S%s%s", s.substring(0, 1),s.substring(1,s.length()),""); // 맨 앞자리만 대문자.
    }
	
	// DB저장을 위한 Y/N 으로 변경하기
	public static boolean isYN(String s) {
		if(StringUtils.equalsIgnoreCase(s, "Y")
				|| StringUtils.equalsIgnoreCase(s, "N"))
			return true;
		return false;
	}
	public static String toYN(String s) {
		if(StringUtils.equals(s,"null") || StringUtils.isEmpty(s)) 
			return "N";

		if(isYN(s))
			return s;

		if(StringUtils.equals(s, "1")
				|| StringUtils.equalsIgnoreCase(s, "true")) 
			return "Y";

		if(StringUtils.equals(s, "0")
				|| StringUtils.equalsIgnoreCase(s, "false")) 
			return "N";

		return "N";
	}

	public static org.json.JSONObject toJSON(String strJsonObject) throws Exception {
		return JSONUtil.toJSON(strJsonObject);
	}
	
	// 널 일 경우 공백으로 채움.
	public static String emptyIfNull(String str) {
		return String.valueOf(ObjectUtil.emptyIfNull(str));
	}
	
	// 소숫점 제거하기 - 정수만 가져오기
	public static int toInt(String str) {
        return (int) Double.parseDouble(str);
	}
	
	// 소숫점 몇번째 자리 지정하기 - 반올림
	public static String toFloat(String str, int decPlaces) {
		if(decPlaces<0) return "-";
        String format = "%."+decPlaces+"f";
        String value = String.format(format,Double.parseDouble(str));
        log.debug("{} => {}", str, value);
        return value;
	}

    /**
     * underscore ('_') 가 포함되어 있는 문자열을 Camel Case 
     * - 낙타등 표기법 - 단어의 변경시에 대문자로 시작하는 형태. 시작은 소문자)
     * 로 변환해주는 Utility 메서드
     * 
     * 커스텀 CamelCase는 CamelUtil.toCamelCase() 사용
     * 
     * @param underScore 
     *        '_' 가 포함된 변수명
     * @return Camel 표기법 변수명
     */
	public static String toCamelCase(String underScore) {
		return JdbcUtils.convertUnderscoreNameToPropertyName(underScore);
	}

}