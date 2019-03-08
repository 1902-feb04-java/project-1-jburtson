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
			out.println("<p> Welcome to the Manager homepage! </p>");
			out.println("<p> Approve/deny reimbursement requests (Coming soon!) </p>");
			out.println("<p> View all reimbursement requests receipt images (Coming soon?) </p>");
			out.println("<p> View all of your resolved reimbursement requests (Coming soon!) </p>");
			out.println("<p> View all employees (Coming soon!) </p>");
			out.println("<p> View all reimbursement requests of a given employee (Coming soon!) </p>");

			try {
				ConnectionSQL connection = new ConnectionSQL();
				Statement statement = connection.getStatement();
				ResultSet rs;

				// Create reimbursement request table
				// ---------------------------------------------
				out.println("<h2>Reimbursement requests:</h2>");

				// change view depending on GET parameters
				String view = request.getParameter("view");
				if (view == null)
					view = "";
				switch (view) {
				case "Pending":
					rs = statement.executeQuery(
							"SELECT * FROM reimbursement_requests WHERE isPending=TRUE");
					break;
				case "Resolved":
					rs = statement.executeQuery(
							"SELECT * FROM reimbursement_requests WHERE isResolved=TRUE AND resolvedBy="+id);
					break;
				default:
					view = "All";
					rs = statement.executeQuery("SELECT * FROM reimbursement_requests");
					break;
				}

				out.println("<table>");
				out.println("<thead>");
				out.println("<tr><th colspan=4> " + view + " Reimbursement Requests </th></tr>");
				out.println("<tr>");
				out.println("<th> Amount </th>");
				out.println("<th> Description </th>");
				out.println("<th> Status </th>");
				out.println("<th> Resolution </th>");
				// out.println("<th> Image </th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				while (rs.next()) {
					// int reburId = rs.getInt("id");
					int reburAmount = (int) rs.getInt("amount");
					String reburDescription = rs.getString("description");
					boolean reburIsResolved = rs.getBoolean("isResolved");
					int reburResolvedBy = rs.getInt("resolvedBy");
					if (reburDescription == null)
						reburDescription = "(blank)";
					out.println("<tr>");
					out.println("<td> $" + reburAmount + " </td>");
					out.println("<td> " + reburDescription + " </td>");
					out.println("<td> " + getStatus(rs) + " </td>");
					if (reburIsResolved)
						out.println("<td> Resolved by " + getResolvedBy(reburResolvedBy) + " </td>"); // get name of manager who resolved request
					else {
						out.println("<td> (unresolved) </td>");
					}
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
	private String getResolvedBy(int id){
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
