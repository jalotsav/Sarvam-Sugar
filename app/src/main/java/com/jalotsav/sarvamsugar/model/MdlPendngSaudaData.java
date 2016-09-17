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
 */

package com.jalotsav.sarvamsugar.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by JALOTSAV Dev. on 18/9/16.
 */
public class MdlPendngSaudaData {

    @SerializedName("SAUDANO")
    String saudaNo;

    @SerializedName("PNAME")
    String pname;

    @SerializedName("DALAL")
    String dalal;

    @SerializedName("ITEM")
    String item;

    @SerializedName("QTYINBORI")
    String qtyInBori;

    @SerializedName("QTY")
    String qty;

    @SerializedName("RATE")
    String rate;

    @SerializedName("AMOUNT")
    String amount;

    @SerializedName("MARKETRATE")
    String marketRate;

    @SerializedName("AREA")
    String area;

    public String getSaudaNo() {
        return saudaNo;
    }

    public void setSaudaNo(String saudaNo) {
        this.saudaNo = saudaNo;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDalal() {
        return dalal;
    }

    public void setDalal(String dalal) {
        this.dalal = dalal;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQtyInBori() {
        return qtyInBori;
    }

    public void setQtyInBori(String qtyInBori) {
        this.qtyInBori = qtyInBori;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(String marketRate) {
        this.marketRate = marketRate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
