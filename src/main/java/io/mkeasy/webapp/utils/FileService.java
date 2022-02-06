package io.mkeasy.webapp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import eu.bitwalker.useragentutils.Browser;
import io.mkeasy.utils.AgentUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileService {
	
	public static void download(HttpServletRequest request, HttpServletResponse response, 
			File file, String fileName) throws Exception {

		if(!file.exists()) return;
		downloadFile(fileName, file, request, response);

	}
	
	private static void downloadFile(String fileName, File file, HttpServletRequest request, HttpServletResponse response) {
		if(!file.exists()) return;
		
		String CHARSET = "UTF-8";
		OutputStream out = null;
		InputStream in = null;
		
		try {
			out = response.getOutputStream();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", getDisposition(fileName, getBrowser(request)));
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Transfer-Encoding", "binary"); 
            in = new FileInputStream(file);
            long size = file.length();
            if (size > 0)
                response.setHeader("Content-Length", String.valueOf(size));
            IOUtils.copyLarge(in, out);
            out.flush();
		} catch (Exception e) {
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
	
	private static String getBrowser(HttpServletRequest request) {
		Browser browser = AgentUtil.getBrowser(request);
        return browser.getName();
    }

    private final static String MSIE = "MSIE";
    private final static String CHROME = "CHROME";
    private final static String OPERA = "OPERA";
    private final static String SAFARI = "SAFARI";
    private final static String FIREFOX = "FIREFOX";

    public HashMap<String, Object> fileMap;

	private static String getDisposition(String filename, String browser) throws Exception {

        String dispositionPrefix = "attachment;filename=";
        String encodedFilename = null;

        if (MSIE.equals(browser)) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (FIREFOX.equals(browser)) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (OPERA.equals(browser)) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (CHROME.equals(browser)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') sb.append(URLEncoder.encode("" + c, "UTF-8"));
                else sb.append(c);
            }
            encodedFilename = sb.toString();
        } else {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        }
        return dispositionPrefix + encodedFilename;
    }

}
