package com.revature;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({ "/ManagerServlet", "/manager" })
public class ManagerServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("<head><link rel=\"stylesheet\" href=\"styles.css\"></head>");
		HttpSession session = request.getSession();
		Integer id = (Integer) session.getAttribute("id");
		Boolean isManager = (Boolean) session.getAttribute("isManager");
		// check if there is a session, aka if someone is logged in already
		if (id == null || isManager == null) {
			out.println("<p> Not logged in! </p>");
			out.println("<a href=\"login.html\">Login</a>");
		}
		else if (!isManager) {
			out.println("<p> Oops! Wrong page. Were you looking for the "
					+ "<a href=\"employee\">employee homepage</a>? </p>");
		}
		else {
			out.println("<p> Welcome to the Manager homepage! </p>");
			out.println("<p> Approve/deny reimbursement requests (Coming soon!) </p>");
			out.println("<p> View all reimbursement requests receipt images (Coming soon?) </p>");
			out.println("<p> View all of your resolved reimbursement requests (Coming soon!) </p>");
			out.println("<p> View all employees (Coming soon!) </p>");
			out.println("<p> View all reimbursement requests of a given employee (Coming soon!) </p>");
			out.println("<a href=\"logout\">Logout</a>");
		}
		out.close();
	}

}
