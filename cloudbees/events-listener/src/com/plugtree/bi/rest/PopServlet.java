package com.plugtree.bi.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plugtree.bi.api.Row;
import com.plugtree.bi.dao.EventDAO;

public class PopServlet extends HttpServlet {

	private static final long serialVersionUID = -5085494696015029883L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Row event = new EventDAO().popOldest();
		
		resp.setHeader("Content-Type", "application/json");
		resp.getWriter().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		resp.getWriter().println("<event>");
		resp.getWriter().println("   <id>" + event.getId() + "</id>");
		resp.getWriter().println("   <key>" + event.getKey() + "</key>");
		resp.getWriter().println("   <value>" + event.getValue1() + "</value>");
		if (event.getValue2() != Float.MIN_VALUE) {
			resp.getWriter().println("   <value>" + event.getValue2() + "</value>");
		}
		if (event.getValue3() != Float.MIN_VALUE) {
			resp.getWriter().println("   <value>" + event.getValue3() + "</value>");
		}
		if (event.getValue4() != Float.MIN_VALUE) {
			resp.getWriter().println("   <value>" + event.getValue4() + "</value>");
		}
		if (event.getValue5() != Float.MIN_VALUE) {
			resp.getWriter().println("   <value>" + event.getValue5() + "</value>");
		}
		if (event.getValue6() != Float.MIN_VALUE) {
			resp.getWriter().println("   <value>" + event.getValue6() + "</value>");
		}
		resp.getWriter().println("</event>");
	}
}
