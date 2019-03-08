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

@WebServlet({ "/EmployeeChangeServlet", "/employeeChange" })
public class EmployeeChangeServlet extends HttpServlet {
	@Override
	public void init() {
		// Load postgresql driver to allow for connecting to database
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("<head><link rel=\"stylesheet\" href=\"styles.css\"></head>");
		
		HttpSession session = request.getSession();
		Integer id = (Integer) session.getAttribute("id");
		Boolean isManager = (Boolean) session.getAttribute("isManager");
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
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				out.println("<h3> Name: "+ firstName + " " + lastName +", Username: "+ username +", Password: "+ password +" </h3>");
			}
			rs.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("UH OH, CONNECTION ERROR");
			e.printStackTrace();
		}
		
		// check that the employee is logged in already
		if (id != null && isManager != null && !isManager) {
			out.println("<form method=post>");
			out.println("new username: <input type=text name='username'><br>");
			out.println("new password: <input type=password name='password'><br>");
			out.println("new first name: <input type=text name='firstName'><br>");
			out.println("new last name: <input type=text name='lastName'><br>");
			out.println("<input type=submit value='Change Info'>");
			out.println("</form>");
		}
		out.println("<a href=\"employee\"> Back to employee homepage </a>");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			HttpSession session = request.getSession();
			Integer id = (Integer) session.getAttribute("id");
			ConnectionSQL connection = new ConnectionSQL();
			Statement statement = connection.getStatement();

			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			
			if (username != null && !"".equals(username)) {
				statement.execute("UPDATE employees SET username='"+username+"' WHERE id="+id);
				out.println("<div>* username updated *</div>");
			}
			if (password != null && !"".equals(password)) {
				statement.execute("UPDATE employees SET password='"+password+"' WHERE id="+id);
				out.println("<div>* password updated *</div>");
			}
			if (firstName != null && !"".equals(firstName)) {
				statement.execute("UPDATE employees SET first_name='"+firstName+"' WHERE id="+id);
				out.println("<div>* first name updated *</div>");
			}
			if (lastName != null && !"".equals(lastName)) {
				statement.execute("UPDATE employees SET last_name='"+lastName+"' WHERE id="+id);
				out.println("<div>* last name updated *</div>");
			}

			connection.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			out.println("<div>* problem updating info *</div>");
			e.printStackTrace();
		}

		doGet(request, response);
	}

}
