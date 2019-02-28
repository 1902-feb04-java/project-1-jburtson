package com.revature;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Employee {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isInserted;

    public Employee(String username, String password, String firstName, String lastName){
    	this.username = username;
    	this.password = password;
    	this.firstName = firstName;
    	this.lastName = lastName;
        // need to run insertSelf in order to get id
    	this.isInserted = false;
    }
    
    public int insertSelf(Statement statement) {
    	if (!isInserted) {
    		String sql = "INSERT INTO employees (username, password, first_name, last_name) " +
                    "VALUES ('"+username+"', '"+password+"', '"+firstName+"', '"+lastName+"')";
        	try {
        		// add request into database
    			statement.executeUpdate(sql);
    			// find what id was given to this new entry
    			sql = "SELECT MAX(id) FROM employees";
    			ResultSet result = statement.executeQuery(sql);
    			// set id
    			if (result.next()) {
    				this.id = result.getInt(1);
    			}
    			
    			result.close();
    			this.isInserted = true;
    			return this.id;
    		} catch (SQLException e) {
    			e.printStackTrace();
    			return -1;
    		}
    	}
    	else {
    		System.err.println("Employee already inserted");
    		return -1;
    	}
    }
    public boolean deleteSelf(Statement statement) {
    	if (isInserted) {
    		String sql = "DELETE FROM employees WHERE id="+this.id;
        	try {
        		// delete from database
    			statement.executeUpdate(sql);
    			this.isInserted = false;
    			this.id=-1;
    			return true;
    		} catch (SQLException e) {
    			e.printStackTrace();
    			return false;
    		}
    	}
    	else{
    		System.err.println("Statment not yet in table");
    		return false;
    	}
    }
    public int getId() {
    	if (isInserted) {
    		return this.id;
    	}
    	else {
    		return -1;
    	}
    }
    
}