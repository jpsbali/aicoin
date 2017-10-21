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

public class DiamondClient implements Runnable {

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
					if (message.contains("DiamondResponse")) {
						System.out.println("Received Message from server: " + message);
						DiamondResponse diamondResponse = gson.fromJson(message, DiamondResponse.class);
						//System.out.println("Diamond Response: " + diamondResponse);
						
						JSONArray resultArr = new JSONArray((String)diamondResponse.getResponseData().getResult());
						
						JSONArray diamondIdsArr = resultArr.getJSONArray(0);
						JSONArray txnIdsArr = resultArr.getJSONArray(1);
						
						/*System.out.println("diamondIdsArr:" + diamondIdsArr);
						System.out.println("txnIdsArr" + txnIdsArr);*/
						
						Map<String, String> diamondTxnMap = new HashMap<String, String>();
						
						for (int i = 0; i < diamondIdsArr.length(); i++) {
							diamondTxnMap.put(diamondIdsArr.getString(i), txnIdsArr.getString(i));
							UpdateDiamondTable(diamondIdsArr.getString(i), txnIdsArr.getString(i));	
						}
						System.out.println("Txn Map:" + diamondTxnMap);
						
						//Steps to do - Kranthi
						//Add a new column to diamond table to save the response
						//save the transaction received for a diamond id to the table
						//change the aibc_status from (New) to (processed)
						//create new history table with colums : CREATE TABLE `diamond_history` (
						//`id` int(11) NOT NULL,
						//`diamond_itemfk` int(11) NOT NULL,
						//`date` date DEFAULT NULL,
						//`description` varchar(45) DEFAULT NULL,
						//PRIMARY KEY (`id`,`diamond_itemfk`),
						//KEY `DiamondItemFK_idx` (`diamond_itemfk`)
						//)
						//commit hana						
					}
					else {
						System.out.println("Filtering out Message Received from server: " + message);
					}					
				}
			});
			
			sendDiamondMessage();
			Thread.sleep(2000);

			Timer timer = new Timer();
			TimerTask myTask = new TimerTask() {
				@Override
				public void run() {
					System.out.println("Timer Sending Fault Message at time :  " + LocalDateTime.now());
					sendDiamondMessage();
				}
			};
			timer.schedule(myTask, 20000, 20000);
			
            // wait forever
            //Thread.currentThread().join();
        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        }
	}
	private void UpdateDiamondTable(String rowHash, String txnID){
		if (hanaStatement != null) {
			try {
				System.out.println("Updating Diamond Table...");
				StringBuilder  sqlsb = new StringBuilder("UPDATE \"8EDD06F21D6243FD92CA9FD2F2FED431\".\"aicoin.db::SAP_HANA_BLOCKCHAIN.DIAMOND\" SET AIBC_STATUS = 'PROCESSED', AIBC_TRANS = '");
				sqlsb.append(txnID);
				sqlsb.append("' WHERE AIBC_STATUS = 'NEW' AND ROWHASH = '");
				sqlsb.append(rowHash);	sqlsb.append("'");	
				System.out.println(sqlsb);
				
				hanaStatement.addBatch(sqlsb.toString());		
				// Commit it to Hana
				hanaDB.CommitToHana();				
			} catch (SQLException e) {
				System.err.println("Diamond table Update failed!");
			}
		}
	}
	private void sendDiamondMessage() {
		// send message to websocket
		JSONObject diamondRequest = new JSONObject();
		diamondRequest.put("command", "saveDiamond");
		JSONObject diamondDataJO = new JSONObject();
		
		JSONArray txnIds = new JSONArray();
		try{
		StringBuilder  sqlsb = new StringBuilder("SELECT ROWHASH FROM \"8EDD06F21D6243FD92CA9FD2F2FED431\".\"aicoin.db::SAP_HANA_BLOCKCHAIN.DIAMOND\" WHERE AIBC_STATUS = 'NEW' LIMIT 5");

		ResultSet resultSet = hanaStatement.executeQuery(sqlsb.toString());
		while(resultSet.next())
		{
			txnIds.put(resultSet.getString("ROWHASH"));
		}
		} catch (SQLException e) {
			System.err.println("Diamond Select Statement failed!");
		}
		
		if (txnIds.length() > 0) {
			diamondDataJO.put("diamondIds", txnIds);
			diamondRequest.put("diamondData", diamondDataJO);
			String newDiamondMsg = diamondRequest.toString();
			System.out.println("Request : " + newDiamondMsg);
			clientEndPoint.sendMessage(newDiamondMsg);	
		}
		/*else
			System.out.println ("No new diamond rows added yet.");*/		
	}
	
	private static String getHash(String str) {
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