package com.revature;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({ "/RegistrationServlet", "/registration" })
public class RegistrationServlet extends HttpServlet {
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
		
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		boolean isManager = ("manager").equals(request.getParameter("isManager"));
		
		out.println("<p> user input "+user+", with password: "+password+" and name "+firstName+" "+lastName+" </p>");
		
		Employee worker = new Employee(user, password, firstName, lastName, isManager);
		
		// try to create SQL connection and add employee to database
		ConnectionSQL connection;
		try {
			connection = new ConnectionSQL();
			worker.insertSelf(connection.getStatement());
			connection.close();
			System.out.println("Employee "+worker.getId()+": inserted successfully.");
			out.println("<p> Registered Successfully :) </p>");
		} catch (SQLException e) {
			System.err.println("Could not insert new employee");
			out.println("<p> Problem registering employee </p>");
			e.printStackTrace();
		}
		
		out.println("<a href=\"login.html\">Back to login</a>");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
