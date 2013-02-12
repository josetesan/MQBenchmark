package com.mcentric.rabbitmq;

import java.io.Serializable;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;

import com.mcentric.JMSProducer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQProducer implements Serializable,JMSProducer {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private ConnectionFactory factory ;
	private ObjectPool <Connection> connectionPool;
	
	public RabbitMQProducer() {
		this.factory = createFactory();
		this.connectionPool = new StackObjectPool<Connection>(new ConnectionPoolFactory(factory));
	}
	
	@Override
	public void run() throws Exception {
		Connection conn = connectionPool.borrowObject();
		Channel channel = conn.createChannel();
		String exchangeName = "myExchange";
		String routingKey = "testRoute";
		byte[] messageBodyBytes = "Hello, world!".getBytes();
		channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
		channel.close();
		connectionPool.returnObject(conn);
	}
	
	
	
	private ConnectionFactory createFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		return factory;
	}
}