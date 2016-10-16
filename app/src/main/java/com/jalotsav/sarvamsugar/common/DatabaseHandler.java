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

package com.jalotsav.sarvamsugar.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by JALOTSAV Dev. on 3/8/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements AppConstants {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "sarvamsugar_mdb";

    SQLiteDatabase mdbHelper;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub

        mdbHelper = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create Balls Table
        String CREATE_TABLE_MASTERDTLS = "CREATE TABLE " + TABLE_MASTER_DETAILS
                + "("
                + KEY_MASTERDTLS_MDID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MASTERDTLS_PCODE + " TEXT,"
                + KEY_MASTERDTLS_PNAME + " TEXT,"
                + KEY_MASTERDTLS_MOBILE + " TEXT,"
                + KEY_MASTERDTLS_PHONE + " TEXT,"
                + KEY_MASTERDTLS_DALAL + " TEXT,"
                + KEY_MASTERDTLS_CPERSON + " TEXT,"
                + KEY_MASTERDTLS_CRLIMIT + " TEXT,"
                + KEY_MASTERDTLS_BORI + " TEXT,"
                + KEY_MASTERDTLS_COUNT + " TEXT,"
                + KEY_MASTERDTLS_AVG + " TEXT,"
                + KEY_MASTERDTLS_LASTBILLDAYS + " TEXT,"
                + KEY_MASTERDTLS_LASTITEM + " TEXT,"
                + KEY_MASTERDTLS_LASTAVG + " TEXT,"
                + KEY_MASTERDTLS_ADD_DATE + " TEXT,"
                + KEY_MASTERDTLS_MODIFY_DATE + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_MASTERDTLS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    //Insert Function
    public long insertTable(ContentValues cv, String tablename){

        Log.i(AppConstants.LOG_TAG, "on Insert time Table Name : " + tablename);
        long id = mdbHelper.insert(tablename, null, cv);
//		mdbHelper.close();
        return id;
    }

    //Update Function
    public void updateTable(ContentValues cv, String tablename, String where_cause){

        Log.i(AppConstants.LOG_TAG, "on Update time Table Name : " + tablename);
        mdbHelper.update(tablename, cv, where_cause, null);
//		db.close();
    }

    // Update query using sql statement
    public void updateUsingSQLStatement(String query) {
        // TODO Auto-generated method stub
        Log.i(AppConstants.LOG_TAG, "on Update using SQL-Query");
        mdbHelper.execSQL(query);
    }

    //Delete All Record from Table Function
    public void deleteAllRecord(String tablename){

        Log.i(AppConstants.LOG_TAG, "on Delete time Table Name : " + tablename);
        mdbHelper.delete(tablename,null, null);
    }

    //Delete Function
    public void deleteRow(String tablename, String columnname, String columnvalue){

        Log.i(AppConstants.LOG_TAG, "on Delete time Table Name : " + tablename);
        mdbHelper.delete(tablename, columnname + "='" + columnvalue + "'", null);
//		db.close();
    }

    //Fire Query and get result in Cursor
    public Cursor GetQueryResult(String query) {
        // TODO Auto-generated method stub
        Cursor cursor = mdbHelper.rawQuery(query, null);
        return cursor;
    }


    // Execute query using sql statement
    public void executeSQLStatement(String query) {
        // TODO Auto-generated method stub
        mdbHelper.execSQL(query);
    }

    // Close database
    public void dbClose(){
        // TODO Auto-generated method stub
        mdbHelper.close();
    }
}
