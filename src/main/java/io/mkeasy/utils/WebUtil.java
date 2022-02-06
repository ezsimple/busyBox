package io.mkeasy.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class WebUtil {

	/* Resource 는 static을 사용할 수 없습니다. */
	@Resource 
	AbstractApplicationContext ctx;

	private String WEB_ROOT = null;
	private String RESOURCE_ROOT = null;
	
	public String getWebRoot() throws IOException {
		if (WEB_ROOT!=null)
			return this.WEB_ROOT;

		org.springframework.core.io.Resource resource = ctx.getResource("/");
		File f = resource.getFile();
		WEB_ROOT = f.getPath();
		return WEB_ROOT;
	}
	
	public String getResourceRoot() throws IOException {
		if (RESOURCE_ROOT!=null)
			return this.RESOURCE_ROOT;

		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		org.springframework.core.io.Resource resource = defaultResourceLoader.getResource("/");
		File f = resource.getFile();
		RESOURCE_ROOT = f.getPath();
		return RESOURCE_ROOT;
	}
	
	@Autowired
	ServletContext context;

	// 1. filePath 는 src/main/webapp 이하의 경로를 쓰면 됩니다.
	// -- 예: filePath = '/WEB-INF/sql/test.json'; 
	// 2. springboot처럼 war, jar내부의 파일을 읽어오는데 사용할 수 있습니다.
	// 3. webRoot가 null 인 경우, 사용하면 됩니다.
	// -- 예: springboot
	public String getRource(String filePath) throws IOException {
		InputStream is = context.getResourceAsStream(filePath);
		String contents = IOUtils.toString(is, "UTF-8"); 
		return contents;
	}
	
}
