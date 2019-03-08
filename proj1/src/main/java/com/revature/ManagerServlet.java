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
import javax.servlet.http.HttpSession;

@WebServlet({ "/ManagerServlet", "/manager" })
public class ManagerServlet extends HttpServlet {
	@Override
	public void init() {
		// Load postgresql driver to allow for connecting to database
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("<head><link rel=\"stylesheet\" href=\"styles.css\"></head>");
		HttpSession session = request.getSession();
		Integer id = (Integer) session.getAttribute("id");
		Boolean isManager = (Boolean) session.getAttribute("isManager");
		// check if there is a session, aka if someone is logged in already
		if (id == null || isManager == null) {
			out.println("<p> Not logged in! </p>");
			out.println("<a href=\"login.html\">Login</a>");
		} else if (!isManager) {
			out.println("<p> Oops! Wrong page. Were you looking for the "
					+ "<a href=\"employee\">employee homepage</a>? </p>");
		} else {
			out.println("<h1> Welcome to the Manager homepage, "+getEmployeeName(id)+"! </h1>");
			out.println("<a href=\"managerEmployeeView\"> View all employees </a>");

			try {
				ConnectionSQL connection = new ConnectionSQL();
				Statement statement = connection.getStatement();
				ResultSet rs;
				
				// Create reimbursement request table
				// ---------------------------------------------
				out.println("<h2>Reimbursement requests:</h2>");

				// change view depending on GET parameters
				String view = request.getParameter("view");
				String employeeView = request.getParameter("employeeView");
				if (view == null)
					view = "";
				switch (view) {
				case "Pending":
					rs = statement.executeQuery(
							"SELECT * FROM reimbursement_requests WHERE isPending=TRUE ORDER BY id");
					break;
				case "Resolved":
					rs = statement.executeQuery(
							"SELECT * FROM reimbursement_requests WHERE isResolved=TRUE AND resolvedBy="+id+" ORDER BY id");
					break;
				default:
					// sort by employee
					if (employeeView != null) {
						int employeeId = Integer.parseInt(employeeView);
						view = getEmployeeName(employeeId);
						rs = statement.executeQuery("SELECT * FROM reimbursement_requests WHERE employee_id="+employeeId+" ORDER BY id");
					}
					// sort by all
					else {
						view = "All";
						rs = statement.executeQuery("SELECT * FROM reimbursement_requests ORDER BY id");
					}
					break;
				}

				out.println("<table>");
				out.println("<thead>");
				out.println("<tr><th colspan=5> " + view + " Reimbursement Requests </th></tr>");
				out.println("<tr>");
				out.println("<th> Amount </th>");
				out.println("<th> Description </th>");
				out.println("<th> Status </th>");
				out.println("<th> Resolution </th>");
				// out.println("<th> Image </th>");
				out.println("<th> Submitted By </th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				while (rs.next()) {
					int reburId = rs.getInt("id");
					int reburAmount = (int) rs.getInt("amount");
					String reburDescription = rs.getString("description");
					boolean reburIsResolved = rs.getBoolean("isResolved");
					int reburResolvedBy = rs.getInt("resolvedBy");
					int reburSubmittedBy = rs.getInt("employee_id");
					if (reburDescription == null)
						reburDescription = "(blank)";
					out.println("<tr>");
					out.println("<td> $" + reburAmount + " </td>");
					out.println("<td> " + reburDescription + " </td>");
					out.println("<td> " + getStatus(rs) + " </td>");
					if (reburIsResolved)
						out.println("<td> Resolved by " + getEmployeeName(reburResolvedBy) + " </td>"); // get name of manager who resolved request
					else {
						out.println("<td>");
						out.println("<form method='post'>");
						out.println("Approve:<input type='radio' name='resolve' value='y"+reburId+"'>");
						out.println("Deny:<input type='radio' name='resolve' value='n"+reburId+"' checked>");
						out.println("<input type='submit'>");
						out.println("</form>");
						out.println("</td>");
					}
					out.println("<td>");
					out.println(" " + getEmployeeName(reburSubmittedBy) + " "); // get name of employee who submitted request
					out.println("<form>");
					out.println("<button type='submit' name='employeeView' value="+reburSubmittedBy+">View All</button>"); // view by employee button
					out.println("</form>");
					out.println("</td>");
					out.println("</tr>");
				}
				rs.close();
				out.println("</tbody>");
				out.println("</table>");
				
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// Table view options!
			out.println("<br><span> Reimbursement request view: </span>");
			out.println("<form>");
			out.println("<input type=\"radio\" name=\"view\" value=\"All\" checked> All");
			out.println("<input type=\"radio\" name=\"view\" value=\"Pending\"> All Pending");
			out.println("<input type=\"radio\" name=\"view\" value=\"Resolved\"> Resolved By You");
			out.println("<input type=\"submit\" value=\"Change View\">");
			out.println("</form>");
		}
		out.println("<a href=\"logout\">Logout</a>");
		out.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer id = (Integer) session.getAttribute("id");
		try {
			ConnectionSQL connection = new ConnectionSQL();
			Statement statement = connection.getStatement();

			StringBuilder resolve = new StringBuilder(request.getParameter("resolve"));
			System.out.println(resolve);
			String approvedStatus;
			if (resolve.charAt(0) == 'y') approvedStatus="TRUE";
			else if (resolve.charAt(0) == 'n') approvedStatus="FALSE";
			else approvedStatus="ERROR";
			
			resolve.deleteCharAt(0);
			statement.executeUpdate("UPDATE reimbursement_requests "
					+ "SET isResolved=TRUE, isPending=FALSE, isApproved="+approvedStatus+", resolvedBy="+id
					+ " WHERE id="+Integer.parseInt(resolve.toString()));
			connection.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		doGet(request, response);
	}

	private String getStatus(ResultSet rs) throws SQLException {
		Boolean isResolved = rs.getBoolean("isResolved");
		Boolean isPending = rs.getBoolean("isPending");
		Boolean isApproved = rs.getBoolean("isApproved");
		if (isPending) {
			return "Pending";
		} else if (isResolved) {
			if (isApproved) {
				return "Approved";
			} else {
				return "Denied";
			}
		}
		return "";
	}
	private String getEmployeeName(int id){
		try {
			ConnectionSQL connection = new ConnectionSQL();
			Statement statement = connection.getStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM employees WHERE id="+id);
			if (rs.next()) {
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				rs.close();
				return firstName +" "+lastName;
			}
			else {
				rs.close();
				return "Employee no longer exists";
			}
		} catch (SQLException e){
			e.printStackTrace();
			return "Error";
		}
	}
}
