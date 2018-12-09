package com.josetesan.brokers.benchmark.rabbitmq;

import org.apache.commons.pool2.BasePooledObjectFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ConnectionPoolFactory  extends BasePooledObjectFactory<Connection>{

	private ConnectionFactory factory = null;
	
	public ConnectionPoolFactory(ConnectionFactory factory) {
		this.factory = factory;
	}

	@Override
	public Connection create() throws Exception {
		return factory.newConnection();
	}

	@Override
	public PooledObject<Connection> wrap(Connection obj) {
		return new DefaultPooledObject<>(obj);
	}

	
}
