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

@WebServlet({ "/LoginCheckServlet", "/logincheck" })
public class LoginCheckServlet extends HttpServlet {
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
		
		if (user != null && password != null) {
			// create session, store id of employee in session
			try {
				ConnectionSQL connection = new ConnectionSQL();
				Statement statement = connection.getStatement();
				// look if there's an employee matching the username/password in the table
				ResultSet rs = statement.executeQuery("SELECT * FROM employees WHERE username='"+ user +"' AND password='"+ password +"'");
				if (rs.next()) {
					// Login successful!
					HttpSession session = request.getSession();
					session.setAttribute("id", rs.getInt("id"));
					boolean isManager = rs.getBoolean("isManager");
					session.setAttribute("isManager", isManager);
					//request.getRequestDispatcher("employee").forward(request, response); // to forward instead
					if (isManager) {
						response.sendRedirect("manager");
					} 
					else {
						response.sendRedirect("employee");
					}
				}
				else {
					out.println("<p> No matching username and/or password. Try logging in again. </p>");
					out.println("<a href=\"login.html\">Back to login</a>");
				}
				rs.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			out.println("<p> Missing name or password. Try logging in again. </p>");
			out.println("<a href=\"login.html\">Back to login</a>");
		}
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
