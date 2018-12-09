package com.josetesan.brokers.benchmark.rabbitmq;

import java.io.Serializable;

import com.josetesan.brokers.benchmark.JMSConsumer;
import com.rabbitmq.client.DefaultConsumer;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import static com.josetesan.brokers.benchmark.ServerConstants.SERVER_IP;

public class RabbitMQConsumer implements Serializable, JMSConsumer {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private ConnectionFactory factory ;
	private ObjectPool <Connection> connectionPool;
	
	
	public RabbitMQConsumer() {
		this.factory = createFactory();
		this.connectionPool = new GenericObjectPool<>(new ConnectionPoolFactory(factory));
	}
	
	/**
	 * @throws Exception
	 */
	@Override
	public Object run() throws Exception {
		Connection conn = null;
		String tag = null;
		try {
			conn = connectionPool.borrowObject();
			try (Channel channel = conn.createChannel()) {
				String exchangeName = "myExchange";
				String queueName = "myQueue";
				String routingKey = "testRoute";
				boolean durable = true;
				channel.exchangeDeclare(exchangeName, "direct", durable);
				channel.queueDeclare(queueName, durable, false, false, null);
				channel.queueBind(queueName, exchangeName, routingKey);
				boolean noAck = false;
				DefaultConsumer consumer = new DefaultConsumer(channel);
				tag = channel.basicConsume(queueName, noAck, consumer);
			}
		} finally {
			connectionPool.returnObject(conn);	
		}
		return tag;
	}
	
	@Override
	public void stop()  {
		connectionPool.close();
	}
	
	private ConnectionFactory createFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		factory.setHost(SERVER_IP);
		factory.setPort(5672);
		return factory;
	}
}