package com.josetesan.brokers.benchmark.rabbitmq;

import java.io.Serializable;

import com.josetesan.brokers.benchmark.JMSConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.josetesan.brokers.benchmark.ServerConstants.SERVER_IP;

public class RabbitMQConsumer implements Serializable, JMSConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private ObjectPool <Connection> connectionPool;
	
	
	public RabbitMQConsumer() {
		ConnectionFactory factory = createFactory();
		this.connectionPool = new GenericObjectPool<>(new ConnectionPoolFactory(factory));
	}
	
	/**
	 * @throws Exception
	 */
	@Override
	public Object run() throws Exception {
		String tag = null;

		Connection conn = connectionPool.borrowObject();
		try (Channel channel = conn.createChannel()) {
			String queueName = "myQueue";
			channel.basicConsume(queueName, false, ((consumerTag, delivery) -> {
				LOGGER.info("Received message {}",new String(delivery.getBody(),"UTF-8"));
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}), consumerTag -> { });
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