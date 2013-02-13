/**
 * 
 */
package com.mcentric.activemq;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;

import com.mcentric.JMSProducer;


/**
 * @author joseluis.sanchez@m-centric.com
 * Created on 23/07/2009
 */
public class ActiveMQProducer  implements Serializable , JMSProducer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4405239442926758477L;
	private ObjectPool <Connection> connectionPool;
    private Destination outQueue = null;

	public ActiveMQProducer() {
		 try {
			 ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("nio://192.168.101.32:61000");
		     outQueue = new ActiveMQQueue("test.queue");    
			 connectionPool = new StackObjectPool<Connection>(new ConnectionPoolFactory(connectionFactory));
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
	public void run() { 
		try {
			
			Connection connection = connectionPool.borrowObject();
			QueueSession session = (QueueSession)connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(outQueue);
			TextMessage message = session.createTextMessage("Hello World");
			producer.send(message);
			connectionPool.returnObject(connection);
		} catch (Exception e ) {
			  throw new RuntimeException(e);
		} 
	}

	
	@Override
	public void stop() throws Exception {
		connectionPool.close();
	}
}
