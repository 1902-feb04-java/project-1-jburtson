package com.revature;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Reimbursement {
    private int id;
    private int amount;
    private int employee_id;
    private boolean isResolved;
    private boolean isPending;
    private boolean isApproved;
    private int resolvedBy;
    private boolean isInserted;
    // receipt_img

    public Reimbursement(int amount, int employee_id){
        this.amount = amount;
        this.employee_id = employee_id;
        this.isResolved = false;
        this.isPending = true;
        // need to run insertSelf in order to get id
        this.isInserted = false;
    }
    
    public int insertSelf(Statement statement) {
    	if (!isInserted) {
    		String sql = "INSERT INTO reimbursement_requests (amount, employee_id) " +
                    "VALUES ("+amount+", "+employee_id+")";
        	try {
        		// add request into database
    			statement.executeUpdate(sql);
    			// find what id was given to this new entry
    			sql = "SELECT MAX(id) FROM reimbursement_requests";
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
    		System.out.println("Reimbursement already inserted");
    		return -1;
    	}
    }
    public boolean deleteSelf(Statement statement) {
    	if (isInserted) {
    		String sql = "DELETE FROM reimbursement_requests WHERE id="+this.id;
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
    
    // resolve request
    public void resolve(int manager_id, boolean isApproved) {
    	this.resolvedBy = manager_id;
    	this.isApproved = isApproved;
    	this.isResolved = true;
    	this.isPending = false;
    }
    public int getId(){
    	if (isInserted) {
    		return this.id;
    	}
    	else {
    		return -1;
    	}
    }
    public boolean getIsResolved() {
    	return isResolved;
    }
    public boolean getIsPending() {
    	return isPending;
    }
    public boolean getIsApproved() {
    	return isApproved;
    }
    public int getResolvedBy() {
    	if (isResolved) {
    		return resolvedBy;
    	}
    	else {
    		return -1;
    	}
    }
}