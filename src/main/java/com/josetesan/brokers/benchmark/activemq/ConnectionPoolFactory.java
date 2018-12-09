package com.josetesan.brokers.benchmark.activemq;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import javax.jms.ConnectionFactory;
import javax.jms.Connection;

public class ConnectionPoolFactory  extends BasePooledObjectFactory<Connection> {

	private ConnectionFactory factory = null;
	
	public ConnectionPoolFactory(ConnectionFactory factory) {
		this.factory = factory;
	}

	@Override
	public Connection create() throws Exception {
		return  factory.createConnection();
	}

	@Override
	public PooledObject<Connection> wrap(Connection obj) {
		return new DefaultPooledObject<>(obj);
	}


}
