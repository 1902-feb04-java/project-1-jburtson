package com.revature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        String url = "jdbc:postgresql://localhost:5432/postgres1";
		String username = "postgres";
        String password = "";
        
        try (Connection connection = DriverManager.getConnection(url,username,password)){
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM employees");
			while (rs.next()) {
				System.out.println(rs.getString("id"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
        }
        
        
        Cli.run();
    }
}
