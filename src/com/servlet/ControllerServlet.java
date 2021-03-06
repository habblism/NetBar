package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.action.Action;
import com.action.ActionFactory;

/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet("/ControllerServlet")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ControllerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		handleAction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		handleAction(request, response);
	}

	protected void handleAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathName = request.getServletPath();
		int index = pathName.indexOf(".");
		String actionName = pathName.substring(1, index);
		String actionClassName = this.getInitParameter(actionName);
		if(actionClassName!=null) {
			Action action = ActionFactory.getActionFactory().getAction(actionClassName);
			response.sendRedirect(action.url(request, response));
		}else {
			response.sendRedirect("error.jsp");
		}
	}

}
