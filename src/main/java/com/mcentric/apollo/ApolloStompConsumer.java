/**
 * 
 */
package com.mcentric.apollo;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;
import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsQueue;

import com.mcentric.JMSConsumer;



/**
 * @author joseluis.sanchez@m-centric.com
 * Created on 23/07/2009
 */
public class ApolloStompConsumer  implements Serializable , JMSConsumer {

	private static final String URI = "tcp://localhost:61613";
	
	private static final long serialVersionUID = 8418778306727988075L;
	
    protected String queueName = null; 
    private Destination inQueue = null;
    private ObjectPool <Connection> connectionPool;
	

	public ApolloStompConsumer() {
		try {
			 StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
			 factory.setBrokerURI(URI);
			 connectionPool = new StackObjectPool<Connection>(new ConnectionPoolFactory(factory));
		} catch (Exception e) {
			 System.exit(-1);
		}
	}

	@Override
	public Object run() throws Exception {
		Connection connection = connectionPool.borrowObject();
		connection.start();
		QueueSession session = (QueueSession)connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		
		inQueue = new StompJmsQueue("/queue/","test.queue");
		MessageConsumer consumer = session.createConsumer(inQueue);
		Message message = consumer.receive(1000);
		connectionPool.returnObject(connection);
		session.close();
		consumer.close();
		return message;
	}

}
