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
 * Created by jalotsav on 15/9/16.
 */
public class MdlOutstandingData {

    @SerializedName("PNAME")
    String pname;

    @SerializedName("MOBILE")
    String mobile;

    @SerializedName("PHONE")
    String phone;

    @SerializedName("DALALNAME")
    String dalalName;

    @SerializedName("DAYS")
    String days;

    @SerializedName("AMOUNT")
    String amount;

    @SerializedName("RECAMT")
    String recAmt;

    @SerializedName("OUTSTANDING")
    String outstanding;

    @SerializedName("INVNO")
    String invNo;

    @SerializedName("DATE")
    String date;

    @SerializedName("DUEDATE")
    String dueDate;

    @SerializedName("AREA")
    String area;

    @SerializedName("ZONE")
    String zone;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDalalName() {
        return dalalName;
    }

    public void setDalalName(String dalalName) {
        this.dalalName = dalalName;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRecAmt() {
        return recAmt;
    }

    public void setRecAmt(String recAmt) {
        this.recAmt = recAmt;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
