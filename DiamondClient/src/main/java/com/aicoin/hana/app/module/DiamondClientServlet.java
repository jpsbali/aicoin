package com.aicoin.hana.app.module;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/diamondclient")
public class DiamondClientServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private DiamondClient diamondClient = null;

	public void init() {
		if (diamondClient == null) {
			diamondClient = new DiamondClient();
			(new Thread(diamondClient)).start();	
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8")) {
			writer.write("Diamond Client is Running!!");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
