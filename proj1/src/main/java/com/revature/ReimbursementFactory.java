package com.revature;

public class ReimbursementFactory {
	private static ReimbursementFactory rf;
	
	public static ReimbursementFactory getInstance() {
		if (rf == null) {
			rf = new ReimbursementFactory();
		}
		return rf;
	}
	public static Reimbursement build(int employee_id, int amount) {
		return new Reimbursement(employee_id, amount);
	}
}
