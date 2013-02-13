package com.mcentric; 

public interface JMSConsumer {

	public Object run() throws Exception;

	public void stop()  throws Exception;;
}
