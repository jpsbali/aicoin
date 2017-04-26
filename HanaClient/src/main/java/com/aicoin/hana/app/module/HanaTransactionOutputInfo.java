package com.aicoin.hana.app.module;

/**
 * HanaTransactionOutputInfo.java
 *
 * Description: Contains AI Coin transaction output information for the SAP HANA 2 demonstration.
 *
 * Copyright (C) Apr 20, 2017, Stephen L. Reed.
 */
public class HanaTransactionOutputInfo {
  // the block number
  private int blockNumber;
  // the transaction index
  private int transactionIndex;
  // the transaction id
  private String transactionId;
  // the transaction output index
  private int transactionOutputIndex;
  // the transaction output address, or "to multisig", "to unknown type"
  private String address;
  // the amount
  private double amount;

  /**
   * Creates a new instance of HanaTransactionOutputInfo.
   *
   * @param parentTransactionId the parent's transaction id
   * @param transactionOutputIndex the transaction output index
   * @param address the transaction output address, or "to multisig", "to unknown type"
   * @param amount the amount
   */
  public HanaTransactionOutputInfo(
          final int blockNumber,
          final int transactionIndex,
          final String transactionId,
          final int transactionOutputIndex,
          final String address,
          final double amount) {
    //Preconditions
    assert blockNumber > 0 : "Block number has to be greater than 0";	
    assert transactionIndex >= 0 : "transactionIndex must not be negative";
    assert StringUtils.isNonEmptyString(transactionId) : "transactionId must be a non-empty string";	
    assert transactionOutputIndex >= 0 : "transactionOutputIndex must not be negative";

	this.blockNumber = blockNumber;
    this.transactionIndex = transactionIndex;
    this.transactionId = transactionId;
    this.transactionOutputIndex = transactionOutputIndex;
    this.address = address;
    this.amount = amount;
  }

  /**
   * Gets the Block Number.
   *
   * @return the block number
   */
  public int getBlockNumber() {
    return blockNumber;
  }
  
  /**
   * Gets the transaction index.
   *
   * @return the transaction index
   */
  public int getTransactionIndex() {
    return transactionIndex;
  }

  /**
   * Gets the transaction id.
   *
   * @return the transaction id
   */
  public String getTransactionId() {
    return transactionId;
  }
  
  /**
   * Gets the transaction output index;
   *
   * @return the transaction output index
   */
  public int getTransactionOutputIndex() {
    return transactionOutputIndex;
  }

  /**
   * Gets the transaction output address, or "to multisig", "to unknown type".
   *
   * @return the transaction output address, or "to multisig", "to unknown type"
   */
  public String getAddress() {
    return address;
  }
  
  /**
   * Gets the amount.
   *
   * @return the amount
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Sets the Block Number.
   *
   * @return None
   */
  public void setBlockNumber(int blockNumber) {
    this.blockNumber = blockNumber;	
  }  
  
  /**
   * Sets the transaction index.
   *
   * @return None
   */
  public void setTransactionIndex(int transactionIndex) {
    this.transactionIndex = transactionIndex;
  }

  /**
   * Sets the transaction id.
   *
   * @return None
   */
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  /**
   * Sets the transaction output index;
   *
   * @return None
   */
  public void setTransactionOutputIndex(int transactionOutputIndex) {
    this.transactionOutputIndex = transactionOutputIndex;
  }

  /**
   * Sets the transaction output address, or "to multisig", "to unknown type".
   *
   * @return None
   */
  public void setAddress(String address) {
    this.address = address;
  }
  
  /**
   * Sets the amount.
   *
   * @return None
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }
  
  /**
   * Returns a string representation of this object.
   *
   * @return a string representation of this object
   */
  @Override
  public String toString() {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
            .append("[HanaTransactionOutputInfo\n")
            .append("  blockNumber: ")
            .append(blockNumber)
            .append("\n  transactionIndex: ")
            .append(transactionIndex)
            .append("\n  transactionId: ")
            .append(transactionId)
            .append("\n  transactionOutputIndex: ")
            .append(transactionOutputIndex)
            .append("\n  address: ")
            .append(address)
            .append("\n  amount: ")
            .append(amount)
            .append("]");
    return stringBuilder.toString();
  }
}
