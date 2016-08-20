/*
 * Copyright 2016 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jalotsav.sarvamsugar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JALOTSAV Dev. on 27/7/16.
 */
public class MdlGodownStockData {

    @SerializedName("Item Name")
    String itemName;

    @SerializedName("Packing")
    String packing;

    @SerializedName("E-1")
    String e1;

    @SerializedName("F-24")
    String f24;

    @SerializedName("H-21")
    String h21;

    @SerializedName("H-22")
    String h22;

    @SerializedName("OST")
    String ost;

    @SerializedName("SHAHPUR")
    String shahpur;

    @SerializedName("Total Stk.")
    String totalStock;

    @SerializedName("Pend.Sauda")
    String pendingSauda;

    @SerializedName("Net Stk.")
    String netStock;

    @SerializedName("Stk.Bori")
    String stkBori;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getE1() {
        return e1;
    }

    public void setE1(String e1) {
        this.e1 = e1;
    }

    public String getF24() {
        return f24;
    }

    public void setF24(String f24) {
        this.f24 = f24;
    }

    public String getH21() {
        return h21;
    }

    public void setH21(String h21) {
        this.h21 = h21;
    }

    public String getH22() {
        return h22;
    }

    public void setH22(String h22) {
        this.h22 = h22;
    }

    public String getOst() {
        return ost;
    }

    public void setOst(String ost) {
        this.ost = ost;
    }

    public String getShahpur() {
        return shahpur;
    }

    public void setShahpur(String shahpur) {
        this.shahpur = shahpur;
    }

    public String getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(String totalStock) {
        this.totalStock = totalStock;
    }

    public String getPendingSauda() {
        return pendingSauda;
    }

    public void setPendingSauda(String pendingSauda) {
        this.pendingSauda = pendingSauda;
    }

    public String getNetStock() {
        return netStock;
    }

    public void setNetStock(String netStock) {
        this.netStock = netStock;
    }

    public String getStkBori() {
        return stkBori;
    }

    public void setStkBori(String stkBori) {
        this.stkBori = stkBori;
    }
}
