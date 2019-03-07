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
        
        try{
			ConnectionSQL connect = new ConnectionSQL();
			Statement statement = connect.getStatement();
			
			Employee employee = testEmployee(statement, "userEmployee", "password123", "first", "last", false);
			Employee manager = testManager(statement, "userManager", "password123", "first", "last", false);
			Reimbursement req = testReimbursement(statement, 0, employee.getId(), manager.getId(), true, true);
			employee.deleteSelf(statement);
			manager.deleteSelf(statement);
			
			connect.close();
		} catch (SQLException e) {
			System.err.println("ERROR");
			e.printStackTrace();
        }
        
        //Cli.run();
    }
	public static Employee testEmployee(Statement statement, String username, String password, String firstName, String lastName, boolean shouldDelete) throws SQLException {
    	Employee worker = new Employee(username,password,firstName,lastName, false);
    	worker.insertSelf(statement);
    	int employeeId = worker.getId();
    	System.out.println("New employee id: "+employeeId);
		
		ResultSet rs = statement.executeQuery("SELECT * FROM employees");
		while (rs.next()) {
			System.out.println("id: " + rs.getString("id") +
					", name: "+ rs.getString("first_name") +" "+rs.getString("last_name"));
		}
		rs.close();
		if (shouldDelete) {
			if(worker.deleteSelf(statement)) {
				System.out.println("Successfully deleted");
			}
		}
		return worker;
    }
    public static Employee testManager(Statement statement, String username, String password, String firstName, String lastName, boolean shouldDelete) throws SQLException {
    	Employee worker = new Employee(username,password,firstName,lastName, true);
    	worker.insertSelf(statement);
    	int managerId = worker.getId();
    	System.out.println("New manager id: "+managerId);
		
		ResultSet rs = statement.executeQuery("SELECT * FROM employees");
		while (rs.next()) {
			System.out.println("id: " + rs.getString("id") +
					", name: "+ rs.getString("first_name") +" "+rs.getString("last_name"));
		}
		rs.close();
		if (shouldDelete) {
			if(worker.deleteSelf(statement)) {
				System.out.println("Successfully deleted");
			}
		}
		return worker;
    }
    public static Reimbursement testReimbursement(Statement statement, int amount, int employeeId, int managerId, boolean isApproved, boolean shouldDelete) throws SQLException {
    	Reimbursement req = new Reimbursement(amount, employeeId, "example description");
    	req.insertSelf(statement);
    	int reqId = req.getId();
    	System.out.println("New request id: "+reqId);
		
    	// resolve request
    	req.resolve(managerId, isApproved);
    	System.out.println("request approved: "+req.getIsApproved());
    	
		ResultSet rs = statement.executeQuery("SELECT * FROM reimbursement_requests");
		while (rs.next()) {
			System.out.println("id: " + rs.getString("id") +
					", amount: "+ rs.getInt("amount") +
					", employee_id: "+rs.getInt("employee_id"));
		}
		rs.close();
		if (shouldDelete) {
			if(req.deleteSelf(statement)) {
				System.out.println("Successfully deleted");
			}
		}
		return req;
	}
}
