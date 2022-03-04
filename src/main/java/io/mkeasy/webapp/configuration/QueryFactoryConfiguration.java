package io.mkeasy.webapp.configuration;

import io.mkeasy.webapp.processor.MyBatisProcessor;
import io.mkeasy.webapp.processor.ProcessorServiceFactory;
import io.mkeasy.webapp.processor.QueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class QueryFactoryConfiguration {

    @Bean
    public MyBatisProcessor myBatisProcessor() {
        return new MyBatisProcessor();
    }

    @Bean
    public ProcessorServiceFactory processorServiceFactory() {
        return new ProcessorServiceFactory();
    }

	@Bean
	public QueryFactory queryFactory() {
		return new QueryFactory();
	}

}
