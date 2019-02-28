package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionSQL {
	static final String url = "jdbc:postgresql://localhost:5432/postgres1";
	static final String username = "postgres";
	static final String password = "EM0T0tron123";
	static final String schema = "proj1";
	static final boolean needsSchema = true;
	private Connection connection;
	private Statement statement;
	
	public ConnectionSQL() throws SQLException {
		this.connection = DriverManager.getConnection(url,username,password);
		this.statement = connection.createStatement();
		
		// add schema
		if (needsSchema) {
			String sql = "SET search_path TO "+schema+", public";
			this.statement.execute(sql);
		}
	}
	
	public Statement getStatement() {
		return statement;
	}
	
	public void close() throws SQLException {
		statement.close();
		connection.close();
	}
}
