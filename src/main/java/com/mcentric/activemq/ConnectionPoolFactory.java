package com.mcentric.activemq;

import org.apache.commons.pool.BasePoolableObjectFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Connection;

public class ConnectionPoolFactory  extends BasePoolableObjectFactory<Connection>{

	private ConnectionFactory factory = null;
	
	public ConnectionPoolFactory(ConnectionFactory factory) {
		this.factory = factory;
	}

	@Override
	public Connection makeObject() throws Exception {
		Connection con = factory.createConnection();
		con.start();
		return con;
	}
	
	@Override
	public void destroyObject(Connection con) throws Exception {
		con.close();
	}
	
}
