package io.mkeasy.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class LocaleUtil {
	
	public static Locale getLocale() {
		return LocaleContextHolder.getLocale();
	}

	// Class에서 messages_ko_KR.properties에 정의된 Locale별 문자열 가져오기
	public static String getMessage(MessageSource messageSource, final String code) {
		return messageSource.getMessage(code, null, getLocale());
	}

}
