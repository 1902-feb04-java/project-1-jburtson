package com.revature;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EmployeeServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		// check if there is a session, aka if someone is logged in already
		PrintWriter out = response.getWriter();
		out.println("<p> Welcome to Employee page </p>");
		out.println("<a href=\"logout\">Logout</a>");
		out.close();
	}
}