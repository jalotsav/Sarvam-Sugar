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

import android.os.Environment;

import java.io.File;

/**
 * Created by JALOTSAV Dev. on 21/7/16.
 */
public interface AppConstants {

    // Log Tag key
    String LOG_TAG = "JSarvamSugar";

    // Log Type
    int LOGTYPE_VERBOSE = 1;
    int LOGTYPE_DEBUG = 2;
    int LOGTYPE_INFO = 3;
    int LOGTYPE_WARN = 4;
    int LOGTYPE_ERROR = 5;

    String API_ROOT_URL = "http://svc.sarsugar.com/";

    // API Method name
    String API_METHOD_GETGODOWNSTK = "getGodownStk";
    String API_METHOD_GETMASTERRPT = "getMasterRpt";
    String API_METHOD_GETDSALESSUMM = "getDSalesSumm";
    String API_METHOD_GETDSAUDASUMM = "getDSaudaSumm";
    String API_METHOD_GETOUTSTAND = "getOutstand";

    // Login static Credentials
    String LOGIN_USERNAME = "jsp";
    String LOGIN_PASSWORD = "19150";

    // Directory File paths
    String EXTRNL_STRG_PATH_STRING = Environment.getExternalStorageDirectory().getAbsolutePath();
    File PATH_SARVAMSUGAR_FILES = new File(EXTRNL_STRG_PATH_STRING + "/SarvamSugar/files");

    // Storage File name
    String ALL_MASTER_DTLS_JSON = "allMasterDtls.json";

    // API response
    String RESULT_ZERO = "0";
    String RESULT_ONE = "1";

    // Request Code
    int REQUEST_PERMSN_STORAGE = 101;

    // Key of Database Tables Columns
    // Master Details Table
//    int TABLE_BALLS_ID = 11;
    String TABLE_MASTER_DETAILS = "master_details";
    // Columns
    String KEY_MASTERDTLS_MDID = "md_id";
    String KEY_MASTERDTLS_PCODE = "pcode";
    String KEY_MASTERDTLS_PNAME = "pname";
    String KEY_MASTERDTLS_MOBILE = "mobile";
    String KEY_MASTERDTLS_PHONE = "phone";
    String KEY_MASTERDTLS_DALAL = "dalal";
    String KEY_MASTERDTLS_CPERSON = "cperson";
    String KEY_MASTERDTLS_CRLIMIT = "crlimit";
    String KEY_MASTERDTLS_BORI = "bori";
    String KEY_MASTERDTLS_COUNT = "count";
    String KEY_MASTERDTLS_AVG = "avg";
    String KEY_MASTERDTLS_LASTBILLDAYS = "last_bill_days";
    String KEY_MASTERDTLS_LASTITEM = "last_item";
    String KEY_MASTERDTLS_LASTAVG = "last_avg";
    String KEY_MASTERDTLS_ADD_DATE = "add_date";
    String KEY_MASTERDTLS_MODIFY_DATE = "modify_date";

    // Key for DatePicker
    int FROM_DATE_DTPICKER = 71;
    int TO_DATE_DTPICKER = 72;
}
