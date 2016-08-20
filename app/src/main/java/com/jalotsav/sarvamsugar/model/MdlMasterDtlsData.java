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
 * Created by JALOTSAV Dev. on 6/8/16.
 */
public class MdlMasterDtlsData {

    @SerializedName("PCODE")
    String pcode;

    @SerializedName("PNAME")
    String pname;

    @SerializedName("MOBILE")
    String mobile;

    @SerializedName("PHONE")
    String phone;

    @SerializedName("DALAL")
    String dalal;

    @SerializedName("CPERSON")
    String cPerson;

    @SerializedName("CRLIMIT")
    String crLimit;

    @SerializedName("BORI")
    String bori;

    @SerializedName("COUNT")
    String count;

    @SerializedName("AVG")
    String avg;

    @SerializedName("LASTBILLDAYS")
    String lastBillDays;

    @SerializedName("LASTITEM")
    String lastItem;

    @SerializedName("LASTAVG")
    String lastAvg;

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

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

    public String getDalal() {
        return dalal;
    }

    public void setDalal(String dalal) {
        this.dalal = dalal;
    }

    public String getcPerson() {
        return cPerson;
    }

    public void setcPerson(String cPerson) {
        this.cPerson = cPerson;
    }

    public String getCrLimit() {
        return crLimit;
    }

    public void setCrLimit(String crLimit) {
        this.crLimit = crLimit;
    }

    public String getBori() {
        return bori;
    }

    public void setBori(String bori) {
        this.bori = bori;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getLastBillDays() {
        return lastBillDays;
    }

    public void setLastBillDays(String lastBillDays) {
        this.lastBillDays = lastBillDays;
    }

    public String getLastItem() {
        return lastItem;
    }

    public void setLastItem(String lastItem) {
        this.lastItem = lastItem;
    }

    public String getLastAvg() {
        return lastAvg;
    }

    public void setLastAvg(String lastAvg) {
        this.lastAvg = lastAvg;
    }
}
