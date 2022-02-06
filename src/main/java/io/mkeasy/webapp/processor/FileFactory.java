package io.mkeasy.webapp.processor;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eu.bitwalker.useragentutils.UserAgent;
import io.mkeasy.resolver.CommandMap;
import io.mkeasy.utils.CalUtil;
import io.mkeasy.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileFactory {
	
	private String UPLOAD_DIR;

	final SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public void setUploadDir(String UPLOAD_DIR) {
		this.UPLOAD_DIR = UPLOAD_DIR;
	}
    
	public String upload(MultipartFile file) throws Exception {
		ArrayList<MultipartFile> files = new ArrayList<>();
		files.add(file);
		ArrayList<String> fileIds = uploadFiles(files);
		String filePath = fileIds.get(0);
		log.debug("upload FilePath => {}", filePath);
		return filePath;
	}

	public void download(HttpServletRequest request, HttpServletResponse response, 
			File file, String dnFileName) throws Exception {
		FileUtil.download(request, response, file, dnFileName);
	}

//	public void download(HttpServletRequest request, HttpServletResponse response, CommandMap commandMap) throws Exception {
//
//    	String dnKey = commandMap.getParam("download.key");
//    	String dir = commandMap.getParam("download.dir");
//
//		String key = String.valueOf(commandMap.getParam("key"));
//		if(!StringUtils.equals(key, dnKey)) {
//			log.error("{} key is mismatch");
//			return;
//		}
//		
//		String fid = commandMap.getParam("fid");
//		if(StringUtils.isEmpty(fid)) {
//			log.error("{} fid is empty");
//			return;
//		}
//		
//		String dnFile = commandMap.getParam("download.file"+fid);
//		String dnFileName = commandMap.getParam("download.file"+fid+"Name");
//		if(StringUtils.isEmpty(dnFile)
//				|| StringUtils.isEmpty(dnFileName)) {
//			log.error("{} download file is not defined");
//			return;
//		}
//		
//		// log.debug("dir={}, dnFile={}, dnFileName={}, dnKey={}", dir, dnFile, dnFileName, dnKey);
//	
//		String filePath = dir + "/" + dnFile;
//		File file = new File(filePath);
//
//		download(request, response, file, dnFileName);
//	}

    private String getFileId() {
    	String fileId = dateFormater.format(new Date());
    	return fileId;
    }

	private ArrayList<String> uploadFiles(List<MultipartFile> files) throws Exception {
		
		if (this.UPLOAD_DIR == null)
			throw new Exception("UPLOAD_DIR을 지정하십시오");

		ArrayList<String> fileIds = new ArrayList<>();

		for (MultipartFile file : files) {
			if (file.isEmpty()) continue;

			String year = String.format("%4d",CalUtil.nowYear());
			String month = String.format("%02d",CalUtil.nowMonth());
			String day = String.format("%02d",CalUtil.nowDay());

			String filePath = year+"/"+month+"/"+day;
			String targetDir = this.UPLOAD_DIR+"/"+filePath;

			FileUtil.forceMkdir(new File(targetDir));

			String origFileName = file.getOriginalFilename();
			String ext = FilenameUtils.getExtension(origFileName);

			String fileId = StringUtils.replace(origFileName,"."+ext,"")+"_"+getFileId()+"."+ext;
			Path path = Paths.get(targetDir + "/" + fileId);

			InputStream is = file.getInputStream();
			String saveFileName = path.toString();

//			log.warn("origFileName : {}, saveFileName : {}, fileId : {}, len : {}"
//				,origFileName, saveFileName, fileId, fileId.length());

			File tmp = File.createTempFile(fileId, ".tmp");
			FileUtils.copyInputStreamToFile(is, tmp);
			File dstFile = new File(saveFileName);
			FileUtils.moveFile(tmp, dstFile);

			fileIds.add(dstFile.getAbsolutePath());
			
			// log.debug("파일 저장 완료 : {}",dstFile.getAbsolutePath());
		}
		return fileIds;
	}

	private final String MSIE = "MSIE";
	private final String CHROME = "CHROME";
	private final String OPERA = "OPERA";
	private final String SAFARI = "SAFARI";
	private final String FIREFOX = "FIREFOX";

	private String getBrowser(HttpServletRequest request) {

		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		String browser = userAgent.getBrowser().getName();

		log.debug("브라우저 : {}",userAgent.getBrowser());

		if(StringUtils.startsWith(browser, MSIE)) return MSIE;
		if(StringUtils.startsWith(browser, CHROME)) return CHROME;
		if(StringUtils.startsWith(browser, OPERA)) return OPERA;
		if(StringUtils.startsWith(browser, SAFARI)) return SAFARI;

		return userAgent.getBrowser().name();

	}

	private String getDisposition(String filename, String browser) throws Exception {

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
