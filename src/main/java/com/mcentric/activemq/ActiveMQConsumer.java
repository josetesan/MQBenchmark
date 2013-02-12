/**
 * 
 */
package com.mcentric.activemq;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;

import com.mcentric.JMSConsumer;



/**
 * @author joseluis.sanchez@m-centric.com
 * Created on 23/07/2009
 */
public class ActiveMQConsumer  implements Serializable, JMSConsumer  {

	private static final long serialVersionUID = 8418778306727988075L;
	
    protected String queueName = null; 
    private Destination inQueue = null;
    private ObjectPool <Connection> connectionPool;
	

	public ActiveMQConsumer() {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("nio://192.168.101.32:61000");
		    inQueue = new ActiveMQQueue("test.queue");    
		    this.connectionPool = new StackObjectPool<Connection>(new ConnectionPoolFactory(connectionFactory));
		} catch (Exception e) {
			 System.exit(-1);
		}
	}

	@Override
	public Object run() throws Exception {
		Connection connection = connectionPool.borrowObject();
		connection.start();
		QueueSession session = (QueueSession)connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(inQueue);
		Message message = consumer.receive(1000);
		connectionPool.returnObject(connection);
		session.close();
		consumer.close();
		return message;
	}

}
