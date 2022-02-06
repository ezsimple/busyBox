package io.mkeasy.webapp.processor;

public interface ProcessorService {
	Object execute(ProcessorParam processorParam ) throws Exception;
}
