/**
 * 
 */
package com.josetesan.brokers.benchmark.activemq;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.josetesan.brokers.benchmark.JMSProducer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import org.apache.commons.pool2.impl.GenericObjectPool;

import static com.josetesan.brokers.benchmark.ServerConstants.SERVER_IP;


/**
 * @author joseluis.sanchez@m-centric.com
 * Created on 23/07/2009
 */
public class ActiveMQProducer  implements Serializable , JMSProducer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4405239442926758477L;
	private GenericObjectPool <Connection> connectionPool;
	private Destination outQueue = null;

	public ActiveMQProducer() {
		 try {
			 ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("nio://"+SERVER_IP+":61000");
			 outQueue = new ActiveMQQueue("test.queue");
			 connectionPool = new GenericObjectPool<>(new ConnectionPoolFactory(connectionFactory));
	        } catch (Exception e) {
	            System.exit(-2);
	        }
	}
 
	
    /*
     * Create connection.
     * Create session from connection; false means session is not transacted.
     * Create producer and text message.
     * Send messages, varying text slightly.
     * Send end-of-messages message.
     * Finally, close connection.
     */
	@Override
	public void run() throws Exception {
		Connection connection = null;
		try {
			connection = connectionPool.borrowObject();
			QueueSession session = (QueueSession)connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(outQueue);
			TextMessage message = session.createTextMessage("Hello World");
			producer.send(message);
		} finally {
			connectionPool.returnObject(connection);
		}
	}

	
	@Override
	public void stop() {
		connectionPool.close();
	}
}
