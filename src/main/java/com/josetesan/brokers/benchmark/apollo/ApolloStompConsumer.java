/**
 * 
 */
package com.josetesan.brokers.benchmark.apollo;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueSession;
import javax.jms.Session;

import com.josetesan.brokers.benchmark.JMSConsumer;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsQueue;

import static com.josetesan.brokers.benchmark.ServerConstants.SERVER_IP;


/**
 * @author joseluis.sanchez@m-centric.com
 * Created on 23/07/2009
 */
public class ApolloStompConsumer  implements Serializable , JMSConsumer {

	private static final String URI = "tcp://"+SERVER_IP+":61613";
	
	private static final long serialVersionUID = 8418778306727988075L;
	
    private ObjectPool <Connection> connectionPool;
	

	public ApolloStompConsumer() {
		try {
			 StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
			 factory.setBrokerURI(URI);
			 connectionPool = new GenericObjectPool<>(new ConnectionPoolFactory(factory));
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
			Destination inQueue = new StompJmsQueue("/queue/","test.queue");
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
