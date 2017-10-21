package com.aicoin.hana.app.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class HanaDB {
	
	private Connection hanaConnection = null;
	private Statement hanaStatement = null;

	public Statement getHanaStatement() {
		try {
			if (hanaConnection == null) {
				hanaConnection = ConnectToHana();
			}
		
			if (hanaStatement == null) {
				hanaStatement = hanaConnection.createStatement();
			}
			
			return hanaStatement;
		} catch (Exception ignorred) {
			return null;
		}
	}

	private Connection ConnectToHana() throws SQLException {
		try {
			Context ctx = new InitialContext();
			Context xmlContext = (Context) ctx.lookup("java:comp/env");
			DataSource ds = (DataSource) xmlContext.lookup("jdbc/DefaultDB");
			Connection hanaConnection = ds.getConnection();
			System.out.println("Connected to HANA2 database");
			return hanaConnection;
		} catch (NamingException ignorred) {
			// could happen if HDB support is not enabled
			return null;
		}
	}

	public void CommitToHana()
	{		
		try{
			hanaStatement.addBatch("commit");
			hanaStatement.executeBatch();
		}catch(SQLException se){
			se.printStackTrace();
			System.err.println("Commit Failed");		
		}
	}

	// NOT SURE ABOUT THIS
	public void DisconnectFromHana()
	{		
		try{
			// One final commit
			CommitToHana();	
			
			if(hanaStatement!=null)
				hanaStatement.close();
		}catch(SQLException se){
		}
		System.err.println("Freed Hana Statement");		
		
		try{
			if(hanaConnection!=null)
				hanaConnection.close();					
		}catch(SQLException se){
			se.printStackTrace();
		}
		System.err.println("Disconnected from Hana");
	}
	
	private String getCurrentUser() throws SQLException {
		String currentUser = "";
		PreparedStatement prepareStatement = hanaConnection.prepareStatement("SELECT CURRENT_USER \"current_user\" FROM DUMMY;");
		ResultSet resultSet = prepareStatement.executeQuery();
		int column = resultSet.findColumn("current_user");
		while (resultSet.next()) {
			currentUser += resultSet.getString(column);
		}
		return currentUser;
	}

	private String getCurrentSchema() throws SQLException {
		String currentSchema = "";
		PreparedStatement prepareStatement = hanaConnection.prepareStatement("SELECT CURRENT_SCHEMA \"current_schema\" FROM DUMMY;");
		ResultSet resultSet = prepareStatement.executeQuery();
		int column = resultSet.findColumn("current_schema");
		while (resultSet.next()) {
			currentSchema += resultSet.getString(column);
		}
		return currentSchema;
	}
}
