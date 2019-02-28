package com.revature;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppTest {
	ConnectionSQL connection;
	int testEmployeeId;
	
	@Before
	public void createConnection() {
		try {
			connection = new ConnectionSQL();
		}catch (SQLException e) {
			System.out.println("OOPS");
			e.printStackTrace();
		}
	}
	
    @Test
    public void shouldAnswerWithTrue(){
        assertTrue( true );
        //assertEquals(testGreeter.greet(),"Hello World!");
    }
    
    @Test
    public void testEmployee() {
    	String username = "user";
    	String password = "pass";
    	String firstName = "first";
    	String lastName = "last";
    	Employee worker = new Employee(username,password,firstName,lastName);
    	worker.insertSelf(connection.getStatement());
    	testEmployeeId = worker.getId();
    	//worker.deleteSelf(connection.getStatement());
    }
    
//    @Test
//    public void testReimbursement() {
//    	int amount = 0;
//    	Reimbursement rebur = new Reimbursement(amount,testEmployeeId);
//    	rebur.insertSelf(connection.getStatement());
//    	//rebur.deleteSelf(connection.getStatement());
//    }
    
    @After
	public void closeConnection() {
    	try {
			connection.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
