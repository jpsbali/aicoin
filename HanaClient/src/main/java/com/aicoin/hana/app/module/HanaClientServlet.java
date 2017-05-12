package com.aicoin.hana.app.module;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hanaclient")
public class HanaClientServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private HanaClient hanaClient = null;

	public void init() {
		if (hanaClient == null) {
			hanaClient = new HanaClient();
			(new Thread(hanaClient)).start();	
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8")) {
			writer.write("Hana AI Blockchain Listener is Running!!");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
