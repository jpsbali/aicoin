package com.aicoin.hana.app.module;

import java.util.ArrayList;
import java.util.List;

/**
   * Contains a HANA 2 demonstration transaction information POJO, and a list of its transaction input POJOs and transaction output POJOs.
   */
  public class HanaTransactionItem {

    // the HANA 2 demonstration transaction information
    private HanaTransactionInfo hanaTransactionInfo;
    // the HANA 2 demonstration transaction input informations
    private List<HanaTransactionInputInfo> hanaTransactionInputInfos;
    // the HANA 2 demonstration transaction output informations
    private List<HanaTransactionOutputInfo> hanaTransactionOutputInfos;

    /**
     * Constructs an empty HanaTransactionItem.
     */
    public HanaTransactionItem() {
    }

    /**
     * Constructs a HanaTransactionItem given its constituents.
     *
     * @param hanaTransactionInfo the HANA 2 demonstration transaction information
     * @param hanaTransactionInputInfos the HANA 2 demonstration transaction input informations
     * @param hanaTransactionOutputInfos the HANA 2 demonstration transaction output informations
     */
    public HanaTransactionItem(
            final HanaTransactionInfo hanaTransactionInfo,
            final List<HanaTransactionInputInfo> hanaTransactionInputInfos,
            final List<HanaTransactionOutputInfo> hanaTransactionOutputInfos) {
      //Preconditions
      assert hanaTransactionInfo != null : "hanaTransactionInfo must not be null";
      assert hanaTransactionInputInfos != null : "hanaTransactionInputInfos must not be null";
      assert !hanaTransactionInputInfos.isEmpty() : "hanaTransactionInputInfos must not be empty";
      assert hanaTransactionOutputInfos != null : "hanaTransactionOutputInfos must not be null";
      assert !hanaTransactionOutputInfos.isEmpty() : "hanaTransactionOutputInfos must not be empty";

      this.hanaTransactionInfo = hanaTransactionInfo;
      this.hanaTransactionInputInfos = hanaTransactionInputInfos;
      this.hanaTransactionOutputInfos = hanaTransactionOutputInfos;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
      return "[HanaTransactionItem " + hanaTransactionInfo.toString() + "]";
    }

    /**
     * Gets the HANA 2 demonstration transaction information.
     *
     * @return the HANA 2 demonstration transaction information
     */
    public HanaTransactionInfo getHanaTransactionInfo() {
      return hanaTransactionInfo;
    }

    /**
     * Sets the HANA 2 demonstration transaction information.
     *
     * @param hanaTransactionInfo the HANA 2 demonstration transaction information
     */
    public void setHanaTransactionInfo(final HanaTransactionInfo hanaTransactionInfo) {
      //Preconditions
      assert hanaTransactionInfo != null : "hanaTransactionInfo must not be null";

      this.hanaTransactionInfo = hanaTransactionInfo;
    }

    /**
     * Gets the HANA 2 demonstration transaction input informations.
     *
     * @return the HANA 2 demonstration transaction input informations
     */
    public List<HanaTransactionInputInfo> getHanaTransactionInputInfos() {
      return hanaTransactionInputInfos;
    }

    /**
     * Sets the HANA 2 demonstration transaction input informations.
     *
     * @param hanaTransactionInputInfos the HANA 2 demonstration transaction input informations
     */
    public void setHanaTransactionInputInfos(final List<HanaTransactionInputInfo> hanaTransactionInputInfos) {
      //Preconditions
      assert hanaTransactionInputInfos != null : "hanaTransactionInputInfos must not be null";
      assert !hanaTransactionInputInfos.isEmpty() : "hanaTransactionInputInfos must not be empty";

      this.hanaTransactionInputInfos = hanaTransactionInputInfos;
    }

    /**
     * Gets the HANA 2 demonstration transaction output informations.
     *
     * @return the HANA 2 demonstration transaction output informations
     */
    public List<HanaTransactionOutputInfo> getHanaTransactionOutputInfos() {
      return hanaTransactionOutputInfos;
    }

    /**
     * Sets the HANA 2 demonstration transaction output informations.
     *
     * @param hanaTransactionOutputInfos the HANA 2 demonstration transaction output informations
     */
    public void setHanaTransactionOutputInfos(final List<HanaTransactionOutputInfo> hanaTransactionOutputInfos) {
      //Preconditions
      assert hanaTransactionOutputInfos != null : "hanaTransactionOutputInfos must not be null";
      assert !hanaTransactionOutputInfos.isEmpty() : "hanaTransactionOutputInfos must not be empty";

      this.hanaTransactionOutputInfos = hanaTransactionOutputInfos;
    }
  }
