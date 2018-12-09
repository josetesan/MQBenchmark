package com.josetesan.brokers.benchmark;

public interface JMSProducer {

	public void run() throws Exception;

	public void stop() throws Exception;
}
