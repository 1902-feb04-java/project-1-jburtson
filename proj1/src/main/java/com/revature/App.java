package com.revature;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App 
{
    public static void main( String[] args )
    {
        String url = "jdbc:postgresql://localhost:5432/postgres1";
		String username = "postgres";
        String password = "EM0T0tron123";
        
        try (Connection connection = DriverManager.getConnection(url,username,password)){
			Statement statement = connection.createStatement();
			
			String sql = "SET search_path TO proj1, public";
			statement.execute(sql);
			
			sql = "INSERT INTO employees (username, password, first_name, last_name) " +
	                   "VALUES ('user', 'password123', 'John', 'Smith')";
			//statement.executeUpdate(sql);
			
			ResultSet rs = statement.executeQuery("SELECT * FROM employees");
			while (rs.next()) {
				System.out.println("id: " + rs.getString("id") +
						", name:"+ rs.getString("first_name") +" "+rs.getString("last_name"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
        }
        
        Cli.run();
    }
}
