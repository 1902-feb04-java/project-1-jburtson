package com.revature;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EmployeeServlet extends HttpServlet {

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
		} else if (isManager) {
			out.println("<p> Oops! Wrong page. Were you looking for the "
					+ "<a href=\"manager\">manager homepage</a>? </p>");
		}
		// employee is logged in
		else {
			out.println("<h1> Welcome to the Employee homepage! </h1>");
			// out.println("<p> Upload an image of your receipt (Coming soon?) </p>");
			// out.println("<p> View your info (Coming soon!) </p>");
			// out.println("<p> Receive an email when a request is resolved! (Coming soon?) </p>");

			try {
				ConnectionSQL connection = new ConnectionSQL();
				Statement statement = connection.getStatement();
				ResultSet rs;

				// View employee info
				// ---------------------------------------------
				out.println("<h2> Your info: </h2>");
				rs = statement.executeQuery("SELECT * FROM employees WHERE id="+id);
				if (rs.next()) {
					String username = rs.getString("username");
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					out.println("<h3> Name: "+ firstName + " " + lastName +", Username: "+ username +" </h3>");
				}
				rs.close();
				// change info
				out.println("<a href=\"employeeChange\">Change your info?</a>");
				
				// Create reimbursement request table
				// ---------------------------------------------
				out.println("<h2>Your reimbursement requests:</h2>");

				// change view depending on GET parameters
				String view = request.getParameter("view");
				if (view == null) view = "";
				switch (view) {
				case "Pending":
					rs = statement.executeQuery("SELECT * FROM reimbursement_requests WHERE employee_id=" + id + " AND isPending=TRUE");
					break;
				case "Resolved":
					rs = statement.executeQuery("SELECT * FROM reimbursement_requests WHERE employee_id=" + id + " AND isResolved=TRUE");
					break;
				default:
					view = "All";
					rs = statement.executeQuery("SELECT * FROM reimbursement_requests WHERE employee_id=" + id);
					break;
				}

				out.println("<table>");
				out.println("<thead>");
				out.println("<tr><th colspan=4> "+view+" Reimbursement Requests </th></tr>");
				out.println("<tr>");
				out.println("<th> Amount </th>");
				out.println("<th> Description </th>");
				out.println("<th> Status </th>");
				out.println("<th> Resolved By </th>");
				// out.println("<th> Image </th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				while (rs.next()) {
					//int reburId = rs.getInt("id");
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
					if (reburIsResolved) out.println("<td> "+getResolvedBy(reburResolvedBy)+" </td>"); // get name of manager who resolved request
					//if (reburIsResolved) out.println("<td> "+reburResolvedBy+" </td>");
					else out.println("<td> (unresolved) </td>");
					out.println("</tr>");
				}
				rs.close();
				out.println("</tbody>");
				out.println("</table>");
				
				connection.close();
			} catch (SQLException e) {
				System.err.println("UH OH, CONNECTION ERROR");
				e.printStackTrace();
			}
			// Table view options!
			out.println("<br><span> Reimbursement request view: </span>");
			out.println("<form>");
			out.println("<input type=\"radio\" name=\"view\" value=\"All\" checked> All");
			out.println("<input type=\"radio\" name=\"view\" value=\"Pending\"> Pending");
			out.println("<input type=\"radio\" name=\"view\" value=\"Resolved\"> Resolved");
			out.println("<input type=\"submit\" value=\"Change View\">");
			out.println("</form>");

			// submit new requests!
			out.println("<h2>Submit a new reimbursement request:</h2>");
			out.println("<form method=post>\n" + "		Amount: <input type=\"number\" name=\"amount\" required><br>\n"
					+ "		Description: <input type=\"textfield\" name=\"description\"><br>\n"
					+ "		<input type=\"submit\" value=\"Submit Request\">\n" + "	</form>");
			// logout!
			out.println("<a href=\"logout\">Logout</a>");
		}
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

			int amount = Integer.valueOf(request.getParameter("amount"));
			String description = request.getParameter("description");
			System.out.println(amount);
			System.out.println(description);

			// create new reimbursement request
			Reimbursement rebur;
			if (description != null && !description.equals(""))
				rebur = new Reimbursement(amount, id, description);
			else
				rebur = new Reimbursement(amount, id);

			rebur.insertSelf(statement);

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