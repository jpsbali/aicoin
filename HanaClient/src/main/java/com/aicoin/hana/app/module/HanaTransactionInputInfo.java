package com.aicoin.hana.app.module;

/**
 * HanaTransactionInputInfo.java
 *
 * Description: Contains AI Coin transaction input information for the SAP HANA 2 demonstration.
 *
 * Copyright (C) Apr 20, 2017, Stephen L. Reed.
 */
public class HanaTransactionInputInfo {
  // the block number
  private int blockNumber;
  // the transaction index
  private int transactionIndex;
  // the transaction id
  private String transactionId;
  // the transaction input index
  private int transactionInputIndex;
  // the parent's transaction id
  private String parentTransactionId;
  // this transaction input's index into its parent transction's outputs
  private int indexIntoParentTransaction;

  /**
   * Creates a new instance of HanaTransactionInputInfo.
   * @param transactionInputIndex the transaction input index
   * @param parentTransactionId the parent's transaction id
   * @param indexIntoParentTransaction this transaction input's index into its parent transction's outputs
   */
  public HanaTransactionInputInfo(
          final int blockNumber,
          final int transactionIndex,
          final String transactionId,  
          final int transactionInputIndex,		  
          final String parentTransactionId,
          final int indexIntoParentTransaction) {
    //Preconditions
    assert blockNumber > 0 : "Block number has to be greater than 0";	
    assert transactionIndex >= 0 : "transactionIndex must not be negative";
    assert StringUtils.isNonEmptyString(transactionId) : "transactionId must be a non-empty string";	
    assert transactionInputIndex >= 0 : "transactionInputIndex must not be negative";
    assert StringUtils.isNonEmptyString(parentTransactionId) : "parentTransactionId must be a non-empty string";
    assert indexIntoParentTransaction >= 0 : "indexIntoParentTransaction must not be negative";

	this.blockNumber = blockNumber;
    this.transactionIndex = transactionIndex;
    this.transactionId = transactionId;
    this.transactionInputIndex = transactionInputIndex;
    this.parentTransactionId = parentTransactionId;
    this.indexIntoParentTransaction = indexIntoParentTransaction;
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

  /** Gets the transaction input index.
   *
   * @return the transaction input index
   */
  public int getTransactionInputIndex() {
    return transactionInputIndex;
  }
  
  /** Gets the parent's transaction id.
   *
   * @return the parent's transaction id
   */
  public String getParentTransactionId() {
    return parentTransactionId;
  }

  /** Gets this transaction input's index into its parent transction's outputs.
   *
   * @return this transaction input's index into its parent transction's outputs
   */
  public int getIndexIntoParentTransaction() {
    return indexIntoParentTransaction;
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

  /** Sets the transaction input index.
   *
   * @return None
   */
  public void setTransactionInputIndex(int transactionInputIndex) {
    this.transactionInputIndex = transactionInputIndex;
  }
  
  /** Sets the parent's transaction id.
   *
   * @return None
   */
  public void setParentTransactionId(String parentTransactionId) {
    this.parentTransactionId = parentTransactionId;
  }

  /** Sets this transaction input's index into its parent transction's outputs.
   *
   * @return None
   */
  public void setIndexIntoParentTransaction(int indexIntoParentTransaction) {
    this.indexIntoParentTransaction = indexIntoParentTransaction;
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
            .append("[HanaTransactionInputInfo\n")
            .append("  blockNumber: ")
            .append(blockNumber)
            .append("\n  transactionIndex: ")
            .append(transactionIndex)
            .append("\n  transactionId: ")
            .append(transactionId)			
            .append("\n  transactionInputIndex: ")
            .append(transactionInputIndex)
            .append("  parentTransactionId: ")
            .append(parentTransactionId)
            .append("\n  indexIntoParentTransaction: ")
            .append(indexIntoParentTransaction)
            .append("]");
    return stringBuilder.toString();
  }
}
