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

@WebServlet({ "/LoginCheckServlet", "/logincheck" })
public class LoginCheckServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Load postgresql driver to allow for connecting to database
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		
		if (user != null && password != null) {
			out.println("<p> TODO: Check login and redirect to employee page </p>");
			// create session, store id of employee in session
			try {
				ConnectionSQL connection = new ConnectionSQL();
				Statement statement = connection.getStatement();
				// look if there's an employee matching the username/password in the table
				ResultSet rs = statement.executeQuery("SELECT * FROM employees WHERE username='"+ user +"' AND password='"+ password +"'");
				if (rs.next()) {
					// Login successful!
					request.getSession().setAttribute("id", rs.getInt("id"));
					//request.getRequestDispatcher("employee").forward(request, response);
					response.sendRedirect("employee");
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
