package com.aicoin.hana.app.module;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;
import org.json.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

import com.google.gson.Gson;

public class DiamondSender implements Runnable
{
   public void run()
   {
     try
     {
       //get a connection to Hana DB
		getConnection();

       //open websocket
       clientEndPoint = new WebsocketClientEndpoint(new URI("ws://aicoin.dyndns.org:20000/wsticker"));
		// Use a stateless JSON serializer/deserializer
        gson = new Gson();

     }
     catch(URISyntaxException exception)
     {
      System.err.println("URISyntaxException exception:"+exception.getMessage());
     }

   
      //start of Sender
      BlockChainSender();
      
   } //end of run()
  
  void BlockChainSender() 
  {
    // try{
     //add sender
     clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler(){
	public void handleMessage(String message) {
     signatureArr = getSignatureFromHanaDB();
	}
    });
 
     //send message to websocket
     clientEndPoint.sendMessage(Arrays.toString(signatureArr));

    // } //end of try
 //   catch (SQLException ex) {
	// 	System.err.println("Exception: " + ex.getMessage());
	// }    
 //   catch (LangException ex) {
	// 	System.err.println("Exception: " + ex.getMessage());
	// }   	
 //   catch (Exception ex) {
	// 	System.err.println("Exception: " + ex.getMessage());
	// }
  
  }  //end of Sender method


  private String[] getSignatureFromHanaDB()
  {
  	
  	ArrayList<String> signatureList = new ArrayList<String>();
  	try{
    String signatureFromDB = "";
    
    // StringBuilder  sqlsb = new StringBuilder("SELECT "ROWHASH" FROM \"8EDD06F21D6243FD92CA9FD2F2FED431\".\"aicoin.db::SAP_HANA_BLOCKCHAIN.DIAMOND\" WHERE STATUS = 'I'");
    PreparedStatement prepareStatement = hanaConnection.prepareStatement( "SELECT ROWHASH FROM \"8EDD06F21D6243FD92CA9FD2F2FED431\".\"aicoin.db::SAP_HANA_BLOCKCHAIN.DIAMOND\" WHERE STATUS = 'I'" );
    ResultSet resultSet = prepareStatement.executeQuery();
    int column = resultSet.findColumn("ROWHASH");
	// System.out.println("123 result set is" +resultSet);
    while(resultSet.next())
    {
       signatureList.add(resultSet.getString(column));
       signatureFromDB += resultSet.getString(column) + "";   
    }
  	}
    catch (Exception ex) {
		System.err.println("Exception: " + ex.getMessage());
		// throw ex;
	}
	return (String[])signatureList.toArray(new String[signatureList.size()]);
  }

  	public void getConnection() {
		try {
			if (hanaConnection == null) {
				hanaConnection = ConnectToHana();
			}
		
			// if (hanaStatement == null) {
			// 	hanaStatement = hanaConnection.createStatement();
			// }
			
			// return hanaConnection;
		} catch (Exception ex) {
			System.err.println("exception: " + ex.getMessage());
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
		} catch (Exception ignorred) {
			// could happen if HDB support is not enabled
			return null;
		}
	}  

  	// Class Variables
	private Statement hanaStatement;
	private WebsocketClientEndpoint clientEndPoint;
	private Gson gson;
	private Connection hanaConnection = null;
	private String[] signatureArr;
} //end of class