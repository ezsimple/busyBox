package io.mkeasy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Component
class ValidateUtil {

//	protected static boolean _required(String s) {
//		if(StringUtils.isEmpty(s))
//			return false;
//		return true;
//	}
	
	protected static boolean _auth(String s) {
		JSONObject obj = new JSONObject();
		if(StringUtils.isEmpty(s))
			return false;

		if("management".equalsIgnoreCase(s)) return true;
		if("sales".equalsIgnoreCase(s)) return true;
		if("logistics".equalsIgnoreCase(s)) return true;
		if("accounting".equalsIgnoreCase(s)) return true;

		return false;
	}

	protected static boolean _maxLength(String s, int max) {
		JSONObject obj = new JSONObject();
		if(StringUtils.isEmpty(s))
			return false;
		boolean chk = (s.length()<=max);
		log.debug("{} : {}({}), {}, {}", "ValidMaxLengCallBack" , s,s.length(),max,chk);
		return chk;
	}

	protected static boolean _minLength(String s, int min) {
		if(StringUtils.isEmpty(s))
			return false;
		boolean chk = (s.length()>=min);
		log.debug("{} : {}({}), {}, {}", "ValidMinLengCallBack" , s,s.length(),min,chk);
		return chk;
	}

	protected static boolean _minmaxLength(String s, int min, int max) {
		if(StringUtils.isEmpty(s))
			return false;
		boolean chk = _minLength(s, min) && _maxLength(s, max);
		log.debug("{} : {}({}), min:{},max:{}, chk:{}", "ValidMinMaxLengCallBack" , s,s.length(),min,max,chk);
		return chk;
	}
			
	protected static boolean _idFormat(String id) {
		// 8~14자의 영문소문자+숫자 조합이면 true
		// return this.optional(element) || value.length > 7 && value.match(/^[a-z0-9]{8,14}$/);
		if(StringUtils.isEmpty(id))
			return false;
		String regex = "^[a-z0-9]{8,14}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(id);
		boolean chk = ((id.length() >=8 && id.length() <= 14) && m.matches());
		log.debug("{} : {}({})", "ValidFormatIDCallBack" , id, chk);
		return chk;
	}
		
	protected static boolean _nameFormat(String name) {
		if(StringUtils.isEmpty(name))
			return false;
		// 공백을 제외한 특수문자 허용 안함 && 2~20자의 글자수 이내이어야 함.
		boolean chk = !_existSpecialChar(name) && _minmaxLength(name, 2, 20);
		log.debug("{} : {}({})", "ValidNameCallBack" , name, chk);
		return chk;
	}

	protected static boolean _nameFormat(String name, int max) {
		if(StringUtils.isEmpty(name))
			return false;
		// 공백을 제외한 특수문자 허용 안함 && 2~max자의 글자수 이내이어야 함.
		boolean chk = !_existSpecialChar(name) && _minmaxLength(name, 2, max);
		log.debug("{} : {}({})", "ValidNameCallBack" , name, chk);
		return chk;
	}

	// 코드명에 사용할 수 있는 문자열 검사 (영문과 일부 특수문자 사용가능)
	protected static boolean _codeNameFormat(String name) {
		if(StringUtils.isEmpty(name))
			return false;

		String regex = "^[a-zA-Z0-9.~!() -_+]{1,50}$"; // 공백 추가
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name);
		boolean chk = ((name.length() >=1 && name.length() <= 50) && m.matches());
		log.debug("{} : {}({})", "ValidCodeNameFormatCallBack" , name, chk);
		return chk;
	}

	protected static boolean _UpperCodeNameFormat(String name) {
		if(StringUtils.isEmpty(name))
			return false;

		String regex = "^[A-Z0-9]{1,10}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name);
		boolean chk = ((name.length() >=1 && name.length() <= 10) && m.matches());
		log.debug("{} : {}({})", "ValidCodeNameFormatCallBack" , name, chk);
		return chk;
	}
		
	protected static boolean _emailFormat(String email) {
		if(StringUtils.isEmpty(email))
			return false;
		Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(email);
		boolean chk = m.matches();

		// 이메일 형식이 옳으면 true
		log.debug("{} : {}({})", "ValidEmailCallBack" , email, chk );
		return chk;
	}

	protected static boolean _existSpecialChar(String s) {
		if(StringUtils.isEmpty(s))
			return false;

		// 특수문자 검사 : 특수 문자 존재하면 true
		boolean chk = true;
		for (int i = 0; i < s.length(); i++) {
			chk = true;
			char c = s.charAt(i);
			if (Character.isAlphabetic(c)) {chk = false; continue;}
			if (Character.isWhitespace(c)) {chk = false; continue;}
			if (Character.isDigit(c)) {chk = false; continue;}
			if (!Character.isLetterOrDigit(c)) {
				if(c!=' '||c!='_'||c!='-'||c!='('||c!=')'||c!='#') { // 일부 특수 문자는 허용한다.(추가 예정)
					log.debug("string={}, specialchar={}",s, c);
					chk = false;
					break;
				}
			}
		}
		log.debug("{} : {}({})", "ValidExistSpecialCharCallBack" , s,  chk);
		return chk;
	}

	protected static boolean _containsAlphabetFormat(String str) {
		// 알파벳 검출
		Pattern p = Pattern.compile(".*[a-zA-Z]+.*"); 
		Matcher m = p.matcher(str);
		boolean chk = m.matches();
		return (chk);
	}

	protected static boolean _passwordFormat(String password) {
		if(StringUtils.isEmpty(password))
			return false;
		// 패스워드가 8자 이상의 영문 대소문자+숫자+특수문자 조합이면 true
		//	^                 # start-of-string
		//	(?=.*[0-9])       # a digit must occur at least once
		//	(?=.*[a-z])       # a lower case letter must occur at least once
		//	(?=.*[A-Z])       # an upper case letter must occur at least once
		//	(?=.*[@#$%^&+=])  # a special character must occur at least once
		//	(?=\S+$)          # no whitespace allowed in the entire string
		//	.{8,}             # anything, at least eight places though
		//	$                 # end-of-string
		Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()-_+=])(?=\\S+$).{8,}$");
		Matcher m = p.matcher(password);
		boolean chk = m.matches();
		log.debug("{} : {}({})", "ValidPasswordCallBack" , password, chk );
		return chk;
	}

	protected static boolean _decimalFormat(String s) {
		if(StringUtils.isEmpty(s))
			return false;
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(s);
		boolean chk = m.matches();
		// log.debug("{} : {}({})", "ValidDecimalCallBack" , s, chk );
		return chk;
	}

	protected static boolean _floatFormat(String s) {
		if(StringUtils.isEmpty(s))
			return false;
		Pattern p = Pattern.compile("^[0-9]*\\.[E0-9]+$"); // 지수식 검사 추가 
		Matcher m = p.matcher(s);
		boolean chk = m.matches();
		// log.debug("{} : {}({})", "ValidFloatCallBack" , s, chk );
		return chk;
	}

	// 소수점이 *.0 로 끝나는 포맷
	protected static boolean _zeroEndFormat(String s) {
		if(StringUtils.isEmpty(s))
			return false;
		Pattern p = Pattern.compile("^[0-9]+\\.0$");
		Matcher m = p.matcher(s);
		boolean chk = m.matches();
		// log.debug("{} : {}({})", "ValidZeroEndCallBack" , s, chk );
		return chk;
	}

	protected static boolean _webUrl(String s) {
		if(StringUtils.isEmpty(s))
			return false;
		Pattern p = Patterns.WEB_URL;
		Matcher m = p.matcher(s);
		boolean chk = m.matches();
		log.debug("{} : {}({})", "ValidWebUrlCallBack" , s, chk );
		return chk;
	}

	protected static boolean _phoneFormat(String s) {
		if(StringUtils.isEmpty(s))
			return false;
		Pattern p = Patterns.PHONE;
		Matcher m = p.matcher(s);
		boolean chk = m.matches();
		log.debug("{} : {}({})", "ValidPhonFormatCallBack" , s, chk );
		return chk;
	}

	// 알파벳+숫자로만 이루어진 경우 : true
	protected static boolean _alphaNumericFormat(String s) {
		if(StringUtils.isEmpty(s))
			return false;
		Pattern p = Patterns.ALPHA_NUMERIC;
		Matcher m = p.matcher(s);
		boolean chk = m.matches();
		log.debug("{} : {}({})", "ValidAlphaNumericCallBack" , s, chk );
		return chk;
	}
	
    // ---------------------------------------------
    // 숫자, 알파벳, 점, 언더바(_) ,공백,하이픈(-) 허용 
    // - 다국어 아이디용
    // ---------------------------------------------
	protected static boolean _normalCharFormat(String s) {
		if(StringUtils.isEmpty(s))
			return false;
		Pattern p = Pattern.compile("^[a-zA-Z0-9_\\- \\.]+$");
		Matcher m = p.matcher(s);
		boolean chk = m.matches();
		log.debug("{} : {}({})", "ValidNormalCharCallBack" , s, chk );
		return chk;
	}
	
	/*
	protected static boolean _idUnique(String id) throws Exception {
		if(StringUtils.isEmpty(id))
			return false;
		
		User user = svc.readUser(id);
		if(user == null)
			return true;
		return false;
	}

	protected static boolean _emailUnique(String email) {
		if(StringUtils.isEmpty(email))
			return false;
		User user = svc.readEmail(email);
		if(user == null)
			return true;
		return false;
	}
	*/

	protected final static String SUCCESS_KEY = "success";
	protected final static String MESSAGE_KEY = "message";
	protected final static String TYPE_KEY = "type";

	public static JSONObject _makeResult(boolean ret, String type, String message) {
		JSONObject obj = new JSONObject();
		obj.put(SUCCESS_KEY,ret);
		obj.put(TYPE_KEY, type);
		if(!ret) {
			obj.put(MESSAGE_KEY, message);
			log.debug("#FAIL:Valid# {}",obj);
		}
		return obj;
	}

	// 성공하고서도 메세지를 전달해야 하는 예외적인 경우 사용 : 
	// 사용자ID 찾기
	public static JSONObject _makeResultWithObject(boolean ret, String type, Object object) {
		JSONObject obj = new JSONObject();
		obj.put(SUCCESS_KEY,ret);
		obj.put(TYPE_KEY, type);
		obj.put(MESSAGE_KEY, object);
		return obj;
	}
	
	public static boolean isSuccess(JSONObject o) {
		if(o==null)
			return false;
		return (boolean) o.get(SUCCESS_KEY);
	}

	public static boolean isFail(JSONObject o) {
		if(o==null)
			return true;
		return !((boolean) o.get(SUCCESS_KEY));
	}

	public static void setMessage(JSONObject o, String message) {
		if(o==null) return;
		o.put(MESSAGE_KEY, message);
	}
	
}
