package com.aicoin.hana.app.module;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import org.json.*;
import java.util.Timer;
import java.util.TimerTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.time.LocalDateTime;

public class FaultClient implements Runnable {

	public void run() {
		try{
			// Get a connection and a statement to HanaDB
			hanaDB = new HanaDB();
			hanaStatement = hanaDB.getHanaStatement();
			
			// open websocket
			clientEndPoint = new WebsocketClientEndpoint(new URI("ws://aicoin.dyndns.org:20000/wsticker"));

            // Use a stateless JSON serializer/deserializer
            gson = new Gson();			
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }			

		// And start the listener
		BlockChainListener();
    }
	
	// Listen to Live AI BlockChain and insert into HANA	
	void BlockChainListener()
	{
	    try {
			// add listener
			clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
				public void handleMessage(String message) {		
					if (message.contains("FaultResponse")) {
						System.out.println("Received Message from server: " + message);
						FaultResponse faultResponse = gson.fromJson(message, FaultResponse.class);
						/*System.out.println("Fault Response: " + faultResponse);
						System.out.println("ResultType: " + faultResponse.getResultType());
						System.out.println("getResult: " + faultResponse.getResponseData().getResult());*/
						
						JSONArray resultArr = new JSONArray((String)faultResponse.getResponseData().getResult());
						
						JSONArray faultIdsArr = resultArr.getJSONArray(0);
						JSONArray txnIdsArr = resultArr.getJSONArray(1);
						
						/*System.out.println("faultIdsArr:" + faultIdsArr);
						System.out.println("txnIdsArr" + txnIdsArr);*/
						
						Map<String, String> faultTxnMap = new HashMap<String, String>();
						
						for (int i = 0; i < faultIdsArr.length(); i++) {
							faultTxnMap.put(faultIdsArr.getString(i), txnIdsArr.getString(i));
							UpdateFaultTable(faultIdsArr.getString(i), txnIdsArr.getString(i));
						}
						System.out.println("Txn Map:" + faultTxnMap);
						
						//Steps to do - Kranthi
						//Add a new column to Fault table to save the response
						//save the transaction received for a fault id to the table
						//change the aibc_status from (New) to (processed)
						//commit hana
					}
					else {
						System.out.println("Filtering out Message Received from server: " + message);
					}
				}
			});
			
			sendFaultMessage();
			Thread.sleep(2000);

			Timer timer = new Timer();
			TimerTask myTask = new TimerTask() {
				@Override
				public void run() {
					System.out.println("Timer Sending Fault Message at time :  " + LocalDateTime.now());
					sendFaultMessage();
				}
			};
			timer.schedule(myTask, 20000, 20000);

            // wait forever
            //Thread.currentThread().join();
        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        }
	}

	private void UpdateFaultTable(String faultSig, String txnID){
		if (hanaStatement != null) {
			try {
				System.out.println("Updating Fault Table...");
				StringBuilder  sqlsb = new StringBuilder("UPDATE \"8EDD06F21D6243FD92CA9FD2F2FED431\".\"aicoin.db::SAP_HANA_BLOCKCHAIN.FAULT\" SET FAULT_AIBC_STATUS = 'PROCESSED', FAULT_AIBC_RESPONSE = '");
				sqlsb.append(txnID);
				sqlsb.append("' WHERE FAULT_AIBC_STATUS = 'NEW' AND FAULT_SIGNATURE = '");
				sqlsb.append(faultSig);	sqlsb.append("'");	
				System.out.println(sqlsb);
				
				hanaStatement.addBatch(sqlsb.toString());		
				// Commit it to Hana
				hanaDB.CommitToHana();				
			} catch (SQLException e) {
				System.err.println("Fault table Update failed!");
			}
		}
	}
	private void sendFaultMessage() {
		JSONArray txnIds = new JSONArray();
		try {
			StringBuilder  sqlsb = new StringBuilder("SELECT FAULT_SIGNATURE FROM \"8EDD06F21D6243FD92CA9FD2F2FED431\".\"aicoin.db::SAP_HANA_BLOCKCHAIN.FAULT\" WHERE FAULT_AIBC_STATUS = 'NEW' LIMIT 5");
    		//String signatureFromDB = "";
    		//PreparedStatement prepareStatement = hanaConnection.prepareStatement("SELECT FAULT_SIGNATURE FROM \"8EDD06F21D6243FD92CA9FD2F2FED431\".\"aicoin.db::SAP_HANA_BLOCKCHAIN.FAULT\" WHERE FAULT_AIBC_STATUS = 'I' LIMIT 5");
			//ResultSet resultSet = prepareStatement.executeQuery();
			//int column = resultSet.findColumn("FAULT_SIGNATURE");

    		ResultSet resultSet = hanaStatement.executeQuery(sqlsb.toString());
			while(resultSet.next())
    		{
    			txnIds.put(resultSet.getString("FAULT_SIGNATURE"));
    		}
		} catch (SQLException e) {
			System.err.println("Fault Select Statement failed!");
		}
		
		if (txnIds.length() > 0) {
			// send message to websocket
			JSONObject faultRequest = new JSONObject();
			faultRequest.put("command", "addFault");
			JSONObject faultDataJO = new JSONObject();
		
			faultDataJO.put("faultIds", txnIds);
			faultRequest.put("faultData", faultDataJO);
			String newFaultMsg = faultRequest.toString();
			System.out.println("Request : " + newFaultMsg);
			clientEndPoint.sendMessage(newFaultMsg);
		}
		/*else
			System.out.println ("No new fault rows added yet.");*/
	}

	private String getHash(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		messageDigest.update(str.getBytes());
	    byte[] hash = messageDigest.digest();
		StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < hash.length; ++i) {
	        String hex = Integer.toHexString(hash[i]);
	        if (hex.length() == 1) {
	            sb.append(0);
	            sb.append(hex.charAt(hex.length() - 1));
	        } else {
	            sb.append(hex.substring(hex.length() - 2));
	        }
	    }
	    return sb.toString();
	}
	

	
	// Class Variables
	private HanaDB hanaDB;
	private Statement hanaStatement;
	private WebsocketClientEndpoint clientEndPoint;
	private Gson gson;
}