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

package com.jalotsav.sarvamsugar.dbtableshelper;

import android.content.ContentValues;
import android.database.Cursor;

import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.DatabaseHandler;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.model.MdlGodownStockData;
import com.jalotsav.sarvamsugar.model.MdlMasterDtlsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JALOTSAV Dev. on 6/8/16.
 */
public class TblMasterDtls implements AppConstants {

    String mdId, pCode, pName, mobile, phone, dalal, cPerson, crLimit, bori, count, avg,
            lastBillDays, lastItem, lastAvg, addDate, modifyDate;

    public String getMdId() {
        return mdId;
    }

    public void setMdId(String mdId) {
        this.mdId = mdId;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
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

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    // Insert Data into Table
    public void insertData(DatabaseHandler dbHndlr) {

        ContentValues cv = new ContentValues();
        cv.put(KEY_MASTERDTLS_PCODE, pCode);
        cv.put(KEY_MASTERDTLS_PNAME, pName);
        cv.put(KEY_MASTERDTLS_MOBILE, mobile);
        cv.put(KEY_MASTERDTLS_PHONE, phone);
        cv.put(KEY_MASTERDTLS_DALAL, dalal);
        cv.put(KEY_MASTERDTLS_CPERSON, cPerson);
        cv.put(KEY_MASTERDTLS_CRLIMIT, crLimit);
        cv.put(KEY_MASTERDTLS_BORI, bori);
        cv.put(KEY_MASTERDTLS_COUNT, count);
        cv.put(KEY_MASTERDTLS_AVG, avg);
        cv.put(KEY_MASTERDTLS_LASTBILLDAYS, lastBillDays);
        cv.put(KEY_MASTERDTLS_LASTITEM, lastItem);
        cv.put(KEY_MASTERDTLS_LASTAVG, lastAvg);
        cv.put(KEY_MASTERDTLS_ADD_DATE, addDate);

        //call function of Database Handler class for insert data into Table
        dbHndlr.insertTable(cv, TABLE_MASTER_DETAILS);
    }

    // Update Data into Table
    public void updateData(DatabaseHandler dbHndlr) {

        ContentValues cv = new ContentValues();
        cv.put(KEY_MASTERDTLS_PNAME, pName);

        String where_cause = KEY_MASTERDTLS_MDID + "=" + mdId;
        dbHndlr.updateTable(cv, TABLE_MASTER_DETAILS, where_cause);
    }

    // Delete All Data from Table
    public void deleteData(DatabaseHandler dbHndlr) {

        dbHndlr.deleteAllRecord(TABLE_MASTER_DETAILS);
    }

    public void insertDBAPIData(ArrayList<MdlMasterDtlsData> arrylstMasterDtlsData, DatabaseHandler dbHndlr) {

        if(!arrylstMasterDtlsData.isEmpty())
            deleteData(dbHndlr);

//        for (MdlMasterDtlsData objMasterDtlsData : arrylstMasterDtlsData) {
        for(int i=0; i<100; i++) {

            MdlMasterDtlsData objMasterDtlsData = arrylstMasterDtlsData.get(i);
            this.pCode = objMasterDtlsData.getPcode();
            this.pName = objMasterDtlsData.getPname();
            this.mobile = objMasterDtlsData.getMobile();
            this.phone = objMasterDtlsData.getPhone();
            this.dalal = objMasterDtlsData.getDalal();
            this.cPerson = objMasterDtlsData.getcPerson();
            this.crLimit = objMasterDtlsData.getCrLimit();
            this.bori = objMasterDtlsData.getBori();
            this.count = objMasterDtlsData.getCount();
            this.avg = objMasterDtlsData.getAvg();
            this.lastBillDays = objMasterDtlsData.getLastBillDays();
            this.lastItem = objMasterDtlsData.getLastItem();
            this.lastAvg = objMasterDtlsData.getLastAvg();
            this.addDate = GeneralFuncations.getcurrentTimestamp();

            insertData(dbHndlr);
        }

    }

    // Get All Data from table
    public Cursor getAllMasterData(DatabaseHandler dbHndlr) {

        return dbHndlr.GetQueryResult("SELECT * FROM " + TABLE_MASTER_DETAILS);
    }
}
