package com.revature;

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
    // receipt_img



    public Reimbursement(int amount, int employee_id){
        this.amount = amount;
        this.employee_id = employee_id;
        this.isResolved = isResolved;
        this.isPending = isPending;
    }
    
    public Reimbursement(int id, int amount, int employee_id, boolean isResolved, boolean isPending, boolean isApproved, int resolvedBy){
        this.id = id;
        this.amount = amount;
        this.employee_id = employee_id;
        this.isResolved = isResolved;
        this.isPending = isPending;
        this.isApproved = isApproved;
        this.resolvedBy = resolvedBy;
    }
    
    public String insertSelf(Statement statement) {
    	String sql = "INSERT INTO reimbursement_requests () " +
                "VALUES ('user', 'password123', 'John', 'Smith')";
    	try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return sql;
    }
}