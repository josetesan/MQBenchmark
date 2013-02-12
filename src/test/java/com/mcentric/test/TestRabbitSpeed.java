package com.mcentric.test;


import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.mcentric.JMSConsumer;
import com.mcentric.JMSProducer;
import com.mcentric.rabbitmq.RabbitMQConsumer;
import com.mcentric.rabbitmq.RabbitMQProducer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

@PerfTest(invocations=10000,threads=4)
public class TestRabbitSpeed {
	
	@Rule
	public ContiPerfRule i = new ContiPerfRule();
	
	
	private static JMSConsumer consumer;
	private static JMSProducer producer;
	private static Delivery delivery = null;

	@BeforeClass
	public static void setUp() throws Exception {
		consumer = new RabbitMQConsumer();
		producer = new RabbitMQProducer();
	}

	@Test
	public void test() {
		try {
			producer.run();
			delivery = (Delivery)consumer.run();
			Assert.assertNotNull(delivery);
		} catch (Exception e) {
			
		}
	}

	
	
	

	
}
