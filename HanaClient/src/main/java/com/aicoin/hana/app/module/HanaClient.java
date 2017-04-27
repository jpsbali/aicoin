package com.aicoin.hana.app.module;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import org.json.*;

import com.google.gson.Gson;

public class HanaClient implements Runnable {

	public void run() {
		try{
			// Get a connection and a statement to HanaDB
			hanaDB = new HanaDB();
			hanaStatement = hanaDB.getHanaStatement();
			
			// open websocket
			clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:20000/wsticker"));
			
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
                    System.out.println(message);

                    JSONObject jsonObj = new JSONObject(message);
					if (jsonObj.has("hanaObjectType")) {
	                    String objectType = jsonObj.getString("hanaObjectType");
	                    JSONObject objectValue = jsonObj.getJSONObject("hanaObject");
	                    
	                    //switch based on type to map to appropriate data sent over the wire.
	                    switch (objectType) {
		                    case "HanaBlockInfo":
								HanaBlockInfo hanaBlockInfo = gson.fromJson(objectValue.toString(), HanaBlockInfo.class);
								System.out.println("hanaBlockInfo = " + hanaBlockInfo);	
								InsertBlockIntoHana(hanaBlockInfo);
	                    		break;
		                    case "HanaTransactionInfo":
		                    	HanaTransactionInfo hanaTransactionInfo = gson.fromJson(objectValue.toString(), HanaTransactionInfo.class);
								System.out.println("hanaTransactionInfo = " + hanaTransactionInfo);	
								InsertTransactionIntoHana(hanaTransactionInfo);
	            				break;
		                    case "HanaTransactionInputInfo":
		                    	HanaTransactionInputInfo hanaTransactionInputInfo = gson.fromJson(objectValue.toString(), HanaTransactionInputInfo.class);
								System.out.println("hanaTransactionInputInfo = " + hanaTransactionInputInfo);
								InsertTransactionInputIntoHana(hanaTransactionInputInfo);		                    	
	            				break;
							case "HanaTransactionOutputInfo":
		                    	HanaTransactionOutputInfo hanaTransactionOutputInfo = gson.fromJson(objectValue.toString(), HanaTransactionOutputInfo.class);
								System.out.println("hanaTransactionOutputInfo = " + hanaTransactionOutputInfo);		
								InsertTransactionOutputIntoHana(hanaTransactionOutputInfo);
	            				break;
	            			default:
								System.out.println("Failed to parse incoming object");
	            				break;
	                    }

						// Commit it to Hana
						hanaDB.CommitToHana();
                    }
                    else {
                    	System.out.println("Failed to parse incoming object " + jsonObj);
					}		
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage("{\"command\":\"startblock\", \"blockNumber\":\"" + GetStartBlockNumber() + "\"}");

            // wait forever
            Thread.currentThread().join();

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        }
	}

	private int GetStartBlockNumber()
	{
		// read the latest block number from the DB and request starting from that number  
		return 1;
	}

	// TODO : Verify Datatypes being inserted into HANA
	private void InsertBlockIntoHana(HanaBlockInfo hanaBlockInfo)
	{
		if (hanaStatement != null) {
			try {
				System.out.println("Inserting records into the table BLOCK ...");
				StringBuilder  sqlsb = new StringBuilder("insert into \"aicoin.db::SAP_HANA_BLOCKCHAIN.BLOCK\" values(");
				sqlsb.append(hanaBlockInfo.getBlockNumber());sqlsb.append(",");
				sqlsb.append(hanaBlockInfo.getBlockVersion());sqlsb.append(",'");
				sqlsb.append(hanaBlockInfo.getBlockMerkleRoot());sqlsb.append("','");
				sqlsb.append(hanaBlockInfo.getBlockTime());sqlsb.append("',");
				sqlsb.append(hanaBlockInfo.getBlockNoOftransactions());sqlsb.append(")");
				System.out.println(sqlsb);
				
				hanaStatement.addBatch(sqlsb.toString());				
			} catch (SQLException e) {
				System.err.println("HanaBlockInfo Insert failed!");
			}
		}
	}

	// TODO : Verify Datatypes being inserted into HANA
	private void InsertTransactionIntoHana(HanaTransactionInfo hanaTransactionInfo)
	{
		if (hanaStatement != null) {
			try {
				System.out.println("Inserting records into the table TRANSACTION ...");
				StringBuilder  sqlsb = new StringBuilder("insert into \"aicoin.db::SAP_HANA_BLOCKCHAIN.TRANSACTION\" values(");
				sqlsb.append(hanaTransactionInfo.getBlockNumber());sqlsb.append(",");
				sqlsb.append(hanaTransactionInfo.getTransactionIndex());sqlsb.append(",'");
				sqlsb.append(hanaTransactionInfo.getTransactionId());sqlsb.append("',");
				sqlsb.append(hanaTransactionInfo.getTransactionNoOfInputs());sqlsb.append(",");
				sqlsb.append(hanaTransactionInfo.getTransactionNoOfOutputs());sqlsb.append(")");
				System.out.println(sqlsb);
				
				hanaStatement.addBatch(sqlsb.toString());				
			} catch (SQLException e) {
				System.err.println("HanaTransactionInfo Insert failed!");
			}
		}
	}

	// TODO : Verify Datatypes being inserted into HANA
	private void InsertTransactionInputIntoHana(HanaTransactionInputInfo hanaTransactionInputInfo)
	{
		if (hanaStatement != null) {
			try {
				System.out.println("Inserting records into the table TRANSACTION INPUT ...");
				StringBuilder  sqlsb = new StringBuilder("insert into \"aicoin.db::SAP_HANA_BLOCKCHAIN.TRANSACTION_INPUT\" values(");
				sqlsb.append(hanaTransactionInputInfo.getBlockNumber());sqlsb.append(",'");
				sqlsb.append(hanaTransactionInputInfo.getTransactionIndex());sqlsb.append(",'");
				sqlsb.append(hanaTransactionInputInfo.getTransactionId());sqlsb.append("',");
				sqlsb.append(hanaTransactionInputInfo.getTransactionInputIndex());sqlsb.append("',");				
				sqlsb.append(hanaTransactionInputInfo.getParentTransactionId());sqlsb.append(",'");
				sqlsb.append(hanaTransactionInputInfo.getIndexIntoParentTransaction());sqlsb.append(")");
				System.out.println(sqlsb);
				
				hanaStatement.addBatch(sqlsb.toString());				
			} catch (SQLException e) {
				System.err.println("Insert failed!");
			}
		}
	}

	// TODO : Verify Datatypes being inserted into HANA
	private void InsertTransactionOutputIntoHana(HanaTransactionOutputInfo hanaTransactionOutputInfo)
	{
		if (hanaStatement != null) {
			try {
				System.out.println("Inserting records into the table TRANSACTION OUTPUT ...");
				StringBuilder  sqlsb = new StringBuilder("insert into \"aicoin.db::SAP_HANA_BLOCKCHAIN.TRANSACTION_OUTPUT\" values(");
				sqlsb.append(hanaTransactionOutputInfo.getBlockNumber());sqlsb.append(",'");
				sqlsb.append(hanaTransactionOutputInfo.getTransactionIndex());sqlsb.append(",'");
				sqlsb.append(hanaTransactionOutputInfo.getTransactionId());sqlsb.append("',");
				sqlsb.append(hanaTransactionOutputInfo.getTransactionOutputIndex());sqlsb.append("',");				
				sqlsb.append(hanaTransactionOutputInfo.getAddress());sqlsb.append(",'");
				sqlsb.append(hanaTransactionOutputInfo.getAmount());sqlsb.append(")");
				System.out.println(sqlsb);
				
				hanaStatement.addBatch(sqlsb.toString());				
			} catch (SQLException e) {
				System.err.println("Insert failed!");
			}
		}
	}
	
	// Class Variables
	private HanaDB hanaDB;
	private Statement hanaStatement;
	private WebsocketClientEndpoint clientEndPoint;
	private Gson gson;
}