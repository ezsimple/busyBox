package io.mkeasy.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanUtil {

	@Resource AbstractApplicationContext ctx;

	public static List<Object> getInstantiatedSigletons(ApplicationContext ctx) {
		List<Object> singletons = new ArrayList<Object>();

		String[] all = ctx.getBeanDefinitionNames();

		ConfigurableListableBeanFactory clbf = ((AbstractApplicationContext) ctx).getBeanFactory();
		int index = 0;
		for (String name : all) {
			Object s = clbf.getSingleton(name);
			if (s != null) {
				singletons.add(s);
				index++;
			    log.debug(name);
			    if("CustomUserService".equalsIgnoreCase(name))
			    	log.debug("===================");
			}
		}
		log.debug("singletons count : {}",index);
		return singletons;
	}
}
