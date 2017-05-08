package com.aicoin.hana.app.module;

import java.util.ArrayList;
import java.util.List;

public class HanaBlockItem {

    // the HANA 2 demonstration block information
    private HanaBlockInfo hanaBlockInfo;
    // the HANA 2 demonstration transaction informations
    private List<HanaTransactionItem> hanaTransactionItems;

    /**
     * Constructs an empty HanaBlockItem.
     */
    public HanaBlockItem() {
    }

    /**
     * Constructs an empty HanaBlockItem.
     *
     * @param hanaBlockInfo
     * @param hanaTransactionItems
     */
    public HanaBlockItem(
            HanaBlockInfo hanaBlockInfo,
            List<HanaTransactionItem> hanaTransactionItems) {
      //Preconditions
      assert hanaBlockInfo != null : "hanaBlockInfo must not be null";
      assert hanaTransactionItems != null : "hanaTransactionItems must not be null";
      assert !hanaTransactionItems.isEmpty() : "hanaTransactionItems must not be empty";

      this.hanaBlockInfo = hanaBlockInfo;
      this.hanaTransactionItems = hanaTransactionItems;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
      return "[HanaBlockItem " + hanaBlockInfo.toString() + "]";
    }

    /**
     * Gets the HANA 2 demonstration block information.
     *
     * @return the HANA 2 demonstration block information
     */
    public HanaBlockInfo getHanaBlockInfo() {
      return hanaBlockInfo;
    }

    /**
     * Sets the HANA 2 demonstration block information.
     *
     * @param hanaBlockInfo the HANA 2 demonstration block information
     */
    public void setHanaBlockInfo(final HanaBlockInfo hanaBlockInfo) {
      //Preconditions
      assert hanaBlockInfo != null : "hanaBlockInfo must not be null";

      this.hanaBlockInfo = hanaBlockInfo;
    }

    /**
     * Gets the HANA 2 demonstration transaction informations.
     *
     * @return the HANA 2 demonstration transaction informations
     */
    public List<HanaTransactionItem> getHanaTransactionItems() {
      return hanaTransactionItems;
    }

    /**
     * Sets the HANA 2 demonstration transaction informations.
     *
     * @param hanaTransactionItems the HANA 2 demonstration transaction informations
     */
    public void setHanaTransactionItems(final List<HanaTransactionItem> hanaTransactionItems) {
      //Preconditions
      assert hanaTransactionItems != null : "hanaTransactionItems must not be null";
      assert !hanaTransactionItems.isEmpty() : "hanaTransactionItems must not be empty";

      this.hanaTransactionItems = hanaTransactionItems;
    }
  }
