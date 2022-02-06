package io.mkeasy.utils;

import org.apache.commons.lang.StringUtils;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
public class ValidUtil extends ValidateUtil {
	
	public static JSONObject makeResult() {
		return _makeResult(false, "field_name is unknown", "message is unknown");
	}
	
	// page auth -> db column auth
	private static String convAuth(String auth) {
		if("management".equalsIgnoreCase(auth)
				|| "sales".equalsIgnoreCase(auth)
				|| "logistics".equalsIgnoreCase(auth)
				|| "accounting".equalsIgnoreCase(auth)) 
			return auth.toUpperCase();
		log.debug("#ERROR# convAuth : {} is UNKNOWN", auth);
		return "UNKNOWN";
	}

	private static JSONObject authCheck(String value) {
		boolean ret = _auth(value);
		final String message = "GROUP is required.";
		if(!ret) log.debug("#ERROR# authCheck : value={}",value);
		return _makeResult(ret, "auth", message);
	}
	
	public static boolean isEmpty(String value) {
		String param = StringUtils.trim(value);
		if(StringUtils.equals(param, "null"))
			return true;
		return StringUtils.isEmpty(param);
	}

	public static boolean underMaxLength(String name, String value, int max) {
		// final String message = "You must enter no more than "+max+" characters.";
		boolean ret = _maxLength(value, max);
		return ret;
	}
	
	public static boolean overMinLength(String name, String value, int min) {
		// final String message = "You must enter at least "+min+" characters.";
		boolean ret = _minLength(value, min);
		return ret;
	}
	
	public static boolean betweenMinMaxLength(String name, String value, int min, int max) {
		// final String message = StringUtils.upperCase(name) + " : You must enter at least "+min+"characters and less than "+max+" characters.";
		boolean ret = _minmaxLength(value, min, max);
		return ret;
	}
	
	public static boolean isIdFormat(String value) {
		// final String message = "ID: Must be a combination of 8 to 14 English lowercase letters + numbers";
		boolean ret = _idFormat(value);
		return ret;
	}
	
	public static boolean isNameFormat(String value) {
		// final String message = "Be within 2~20 characters, not special characters.";
		boolean ret = _nameFormat(value);
		return ret;
	}

	public static boolean isNameFormat(String value, int max) {
		// final String message = "Be within 2~"+max+" characters, not special characters.";
		boolean ret = _nameFormat(value, max);
		return ret;
	}

	public static boolean isCodeNameFormat(String value) {
		// final String message = "Code name can only be a few special characters in English.";
		boolean ret = _codeNameFormat(value);
		return ret;
	}

	private static JSONObject upperCodeNameFormat(String value) {
		boolean ret = _UpperCodeNameFormat(value);
		final String message = "Code name can only uppercase English and Digits.";
		if(!ret) log.debug("#ERROR# upperCodeNameFormat : name={}",value);
		return _makeResult(ret, "name", message);
	}

	public static boolean isEmailFormat(String value) {
		// final String message = "EMAIL : Not in email format.";
		boolean ret = _emailFormat(value);
		return ret;
	}

	public static boolean isUrlFormat(String value) {
		// final String message = "Not Web Address format.";
		boolean ret = _webUrl(value);
		return ret;
	}

	public static boolean isPhoneNumberFormat(String name, String value) {
		// final String message = StringUtils.upperCase(name) + " : Not valid Phone number format.";
		boolean ret = _phoneFormat(value);
		return ret;
	}
	
	public static boolean isPasswordFormat(String value) {
		// final String message = "PASSWORD : Combinations of 8 to 14 characters, including uppercase, lowercase, and numbers in English";
		// final String message = "The password must be a combination of at least 8 characters in English and lowercase letters plus numbers plus special characters.";
		boolean ret = _passwordFormat(value);
		return ret;
	}
	
	private static JSONObject passwordConfirm(String password, String pass_confirm) {
		boolean ret = StringUtils.equals(password, pass_confirm);
		final String message = "PASSWORD CONFIRM : Password does not match.";
		if(!ret) log.debug("#ERROR# passwordConfirm : password={}, pass_confirm={}",password,pass_confirm);
		return _makeResult(ret, "pass_confirm", message);
	}

	public static boolean isDecimalFormat(String value) {
		return _decimalFormat(value);
	}

	public static boolean isFloatFormat(String value) {
		return _floatFormat(value);
	}

	public static boolean isZeroEndFormat(String value) {
		return _zeroEndFormat(value);
	}

	public static boolean isAlphaNumericFormat(String value) {
		return _alphaNumericFormat(value);
	}

	public static boolean isNormalCharFormat(String value) {
		return _normalCharFormat(value);
	}

	/*
	public static JSONObject idUnique(String q) throws Exception {
		boolean ret = _idUnique(q);
		final String message = "이미 존재하는 아이디 입니다.";
		return _makeResult(ret, message);
	}

	public static JSONObject emailUnique(String q) {
		boolean ret = _emailUnique(q);
		final String message = "이미 존재하는 이메일 입니다.";
		return _makeResult(ret, message);
	}
	*/

	public static boolean hasSpecialChar(String value) {
		return _existSpecialChar(value);
	}


}
