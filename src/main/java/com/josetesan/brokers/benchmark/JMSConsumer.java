package com.josetesan.brokers.benchmark;

public interface JMSConsumer {

	public Object run() throws Exception;

	public void stop()  throws Exception;
}
