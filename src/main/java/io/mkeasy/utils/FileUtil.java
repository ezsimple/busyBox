package io.mkeasy.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import io.mkeasy.webapp.utils.FileService;


public class FileUtil extends FileUtils {

	public static boolean forceMkdirQuietly(File directory) {
		if (!(directory.exists())) {
			synchronized (Shell.class) {
				if (!(directory.exists())) {
					return directory.mkdirs();
				}
			}
		}
		return (!(directory.isFile()));
	}

	private static final class DirFilter implements FileFilter {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	}
	
	public static String getDailyPath(Date theDay) {
		int yyyy = DateUtil.nowYear();
		int mm = DateUtil.nowMonth();
		int dd = DateUtil.nowDay();

		if(theDay != null) {
			final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			final Calendar c = Calendar.getInstance();
			try {
				c.setTime(theDay);
				yyyy = c.get(Calendar.YEAR);
				mm = c.get(Calendar.MONTH) + 1;
				dd = c.get(Calendar.DAY_OF_MONTH);
			} 
			catch (Exception e) {}
		}
		
		String year = String.format("%04d", yyyy);
		String month = String.format("%02d", mm);
		String day = String.format("%02d", dd);

		return year + "/" + month + "/" + day;
	}
	
	public static String getDailyPath() {
		return getDailyPath(DateUtil.getNow());
	}

	public static void forceMkdir(File dir) {
		if(!dir.exists()) {
			synchronized (FileUtil.class) {
				if(!dir.exists()) {
					dir.mkdirs();
				}
			}
		}
		return;
	}
	
	// 멀티쓰레드 환경에서도 안전하게 파일 Writing 이 되어야 합니다.
	public static void write(final String filePath, final String txt, final boolean append) throws IOException {

        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        
        FileChannel fileChannel = FileChannel.open(
        		path
        		, StandardOpenOption.CREATE
        		, append?StandardOpenOption.APPEND:StandardOpenOption.WRITE);
        
        Charset charset = Charset.defaultCharset();
        ByteBuffer byteBuffer = charset.encode(txt);
        fileChannel.write(byteBuffer);
        fileChannel.close();

	}
	
	public static void download(HttpServletRequest request, HttpServletResponse response, File file, String fileName) throws Exception {
		FileService.download(request, response, file, fileName);
	}

}