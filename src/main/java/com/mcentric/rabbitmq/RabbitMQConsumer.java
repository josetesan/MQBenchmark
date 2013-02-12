package com.mcentric.rabbitmq;

import java.io.Serializable;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;

import com.mcentric.JMSConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class RabbitMQConsumer implements Serializable, JMSConsumer {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private ConnectionFactory factory ;
	private ObjectPool <Connection> connectionPool;
	
	
	public RabbitMQConsumer() {
		this.factory = createFactory();
		this.connectionPool = new StackObjectPool<Connection>(new ConnectionPoolFactory(factory));
	}
	
	/**
	 * @throws Exception
	 */
	@Override
	public Object run() throws Exception {
		
		Delivery delivery = null;
		
		Connection conn = connectionPool.borrowObject();
		Channel channel = conn.createChannel();
		String exchangeName = "myExchange";
		String queueName = "myQueue";
		String routingKey = "testRoute";
		boolean durable = true;
		channel.exchangeDeclare(exchangeName, "direct", durable);
		channel.queueDeclare(queueName, durable, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		boolean noAck = false;
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, noAck, consumer);
		try {
			delivery = consumer.nextDelivery();
		} catch (InterruptedException ie) {
		}
		//System.out.println("Message received" + new String(delivery.getBody()));
		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		channel.close();
		connectionPool.returnObject(conn);
		return delivery;
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