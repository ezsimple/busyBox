package io.mkeasy.utils;

import org.springframework.util.Base64Utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
public class LicenseUtil {

	public static JSONObject check(final String encKey) throws Exception {
		byte[] decKeyBytes = Base64Utils.decodeFromString(encKey);

		String jsonObj = null;
		try {
			final String key = CryptoUtil.getKey();
			jsonObj = CryptoUtil.decrypt(decKeyBytes, key);
		} catch (javax.crypto.IllegalBlockSizeException e) { // 라이센스키가 회손된 경우
			throw e;
		} catch (javax.crypto.BadPaddingException e) { // 디벨로퍼 라이센스의 경우
			try {
				jsonObj = CryptoUtil.decrypt(decKeyBytes, "IGNORE");
			}catch (javax.crypto.BadPaddingException ex) { // 라이센스키가 잘못된 경우
				throw ex;
			}
		} catch (Exception e) {
			// java.lang.ArrayIndexOutOfBoundsException: (key=value 에서 key만 존재하고, value가 없는 경우
			// java.lang.NullPointerException : license.properties 파일만 존재하는 경우 (내용 없는 경우)
			throw e;
		}
		log.debug("license : {}", jsonObj);
	    return JSONObject.fromObject(jsonObj);
	}

}
