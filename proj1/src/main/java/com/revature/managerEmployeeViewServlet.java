package com.revature;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({ "/managerEmployeeViewServlet", "/managerEmployeeView" })
public class managerEmployeeViewServlet extends HttpServlet {
	@Override
	public void init() {
		// Load postgresql driver to allow for connecting to database
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("<head><link rel=\"stylesheet\" href=\"styles.css\"></head>");
		try {
			ConnectionSQL connection = new ConnectionSQL();
			Statement statement = connection.getStatement();
			ResultSet rs;
			rs = statement.executeQuery("SELECT * FROM employees WHERE isManager=FALSE");
			
			out.println("<table>");
			out.println("<thead>");
			out.println("<tr><th colspan=3> Employees </th></tr>");
			out.println("<tr>");
			out.println("<th> Name </th>");
			out.println("<th> Reciepts # </th>");
			out.println("<th> View </th>");
			//out.println("<th> First Name </th>");
			//out.println("<th> Last Name </th>");
			//out.println("<th> Requests </th>");
			//out.println("<th> View Requests </th>");
			out.println("</tr>");
			out.println("</thead>");
			out.println("<tbody>");
			while (rs.next()) {
				int employeeId = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				out.println("<tr>");
				out.println("<td> " + firstName + " " + lastName + " </td>");
				out.println("<td> " + getNumReceipts(employeeId) + " </td>");
				// button to return to view recipts of this employee
				out.println("<td> <form action=manager> <button name='employeeView' type='submit' value="+employeeId+">View Receipts</button> </form> </td>");
				out.println("</tr>");
			}
			out.println("</tbody>");
			out.println("</table>");
			
			rs.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("UH OH, CONNECTION ERROR");
			e.printStackTrace();
		}
		out.println("<a href=\"manager\">Back to manager homepage</a>");
		out.close();
	}
	
	private int getNumReceipts(int id) {
		try {
			ConnectionSQL connection = new ConnectionSQL();
			Statement statement = connection.getStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(id) AS \"num\" FROM reimbursement_requests WHERE employee_id="+id);
			if (rs.next()) {
				int num = rs.getInt("num");
				rs.close();
				return num;
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return -1;
	}

}
