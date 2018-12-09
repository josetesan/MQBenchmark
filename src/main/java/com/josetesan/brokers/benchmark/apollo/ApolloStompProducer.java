/**
 * 
 */
package com.josetesan.brokers.benchmark.apollo;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.josetesan.brokers.benchmark.JMSProducer;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsQueue;

import static com.josetesan.brokers.benchmark.ServerConstants.SERVER_IP;


/**
 * @author joseluis.sanchez@m-centric.com
 * Created on 23/07/2009
 */
public class ApolloStompProducer  implements Serializable , JMSProducer {

	private static final String URI = "tcp://"+SERVER_IP+":61000";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4405239442926758477L;
	private ObjectPool <Connection> connectionPool;
    private Destination outQueue = null;

	public ApolloStompProducer() {
		 try {
			 StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
			 factory.setBrokerURI(URI);
			 connectionPool = new GenericObjectPool<>(new ConnectionPoolFactory(factory));
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
			outQueue = 	new StompJmsQueue("/queue/","test.queue");
			MessageProducer producer = session.createProducer(outQueue);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			TextMessage message = session.createTextMessage("Hello World");
			producer.send(message);
		} finally {
			connectionPool.returnObject(connection);
		}

	}

	
	@Override
	public void stop() throws Exception {
		connectionPool.close();
	}
}
