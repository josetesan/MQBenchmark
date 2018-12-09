/**
 * 
 */
package com.josetesan.brokers.benchmark.activemq;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueSession;
import javax.jms.Session;

import com.josetesan.brokers.benchmark.JMSConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import static com.josetesan.brokers.benchmark.ServerConstants.SERVER_IP;


/**
 * @author joseluis.sanchez@m-centric.com
 * Created on 23/07/2009
 */
public class ActiveMQConsumer  implements Serializable, JMSConsumer {

	private static final long serialVersionUID = 8418778306727988075L;
	
    private Destination inQueue = null;
    private ObjectPool <Connection> connectionPool;
	

	public ActiveMQConsumer() {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("nio://"+SERVER_IP+":61000");
			inQueue = new ActiveMQQueue("test.queue");
			this.connectionPool = new GenericObjectPool<>(new ConnectionPoolFactory(connectionFactory));
		} catch (Exception e) {
			 System.exit(-1);
		}
	}

	@Override
	public Object run() throws Exception {
		Connection connection = null;
		try {
			connection = connectionPool.borrowObject();
			QueueSession session = (QueueSession)connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(inQueue);
			Message message = consumer.receive(1000);
			session.close();
			consumer.close();
			return message;
		} finally {
			connectionPool.returnObject(connection);	
		}
		
	}

	@Override
	public void stop()  {
		connectionPool.close();
	}
}
