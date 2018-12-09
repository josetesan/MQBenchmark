package com.josetesan.brokers.benchmark.rabbitmq;

import java.io.Serializable;


import com.josetesan.brokers.benchmark.JMSProducer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import static com.josetesan.brokers.benchmark.ServerConstants.SERVER_IP;

public class RabbitMQProducer implements Serializable, JMSProducer {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private ObjectPool<Connection> connectionPool;
	
	public RabbitMQProducer() {
		ConnectionFactory factory = createFactory();
		this.connectionPool = new GenericObjectPool<>(new ConnectionPoolFactory(factory));
	}
	
	@Override
	public void run() throws Exception {
		Connection conn = null;
		try {
			conn = connectionPool.borrowObject();
            try (Channel channel = conn.createChannel()) {
                String exchangeName = "myExchange";
                String routingKey = "testRoute";
                byte[] messageBodyBytes = "Hello, world!".getBytes();
                channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, messageBodyBytes);
            }
        } finally {
			connectionPool.returnObject(conn);
		}
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