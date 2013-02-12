package com.mcentric.rabbitmq;

import org.apache.commons.pool.BasePoolableObjectFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionPoolFactory  extends BasePoolableObjectFactory<Connection>{

	private ConnectionFactory factory = null;
	
	public ConnectionPoolFactory(ConnectionFactory factory) {
		this.factory = factory;
	}

	@Override
	public Connection makeObject() throws Exception {
		return factory.newConnection();
	}
	
	@Override
	public void destroyObject(Connection con) throws Exception {
		con.close();
	}
	
}
