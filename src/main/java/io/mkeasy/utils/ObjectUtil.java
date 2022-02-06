package io.mkeasy.utils;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.ObjectUtils;

public class ObjectUtil {

	// 디버깅용
	// Obect 객체를, String 형태로 출력해 준다.
	// 예 : l2u.cmm.usr.dom.User@759bc41f 형태를 
	// ==> 이렇게 표시해준다. l2u.cmm.usr.dom.User@759bc41f[username=devel1,pasㅏsword=$2a$10$s2eqytAKIW/xen4ZmoaoVerzQjpNzldcPrgk0bwq5b3JcacsU7tK6,name=개발자1,isAccountNonExpired=true,isAccountNonLocked=true,isCredentialsNonExpired=true,isEnabled=true,authorities=<null>,email=devel1@link2us.co.kr,phone=00000000001,regdate=Sun Nov 18 20:31:17 EST 2018,asgndate=Sun Nov 18 20:31:17 EST 2018,moddate=Sun Nov 18 20:31:17 EST 2018,pchgdate=Sun Nov 18 20:21:17 EST 2018,isDel=false]
	public static String toString(Object object) {
		return ReflectionToStringBuilder.toString(object);
	}

	// 객체가 널일 경우, 공백으로 치환하기
	public static <T> Object emptyIfNull(T object) {
		return ObjectUtils.defaultIfNull(object, "");
	}
	
}
