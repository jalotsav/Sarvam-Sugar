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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.jalotsav.sarvamsugar.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by JALOTSAV Dev. on 26/7/16.
 */
public class GeneralFuncations {

    /**
     *  Show/Cancel Toast
     **/
    static Toast toast = null;

    public static void showtoastLngthshort(Context context, String message) {
        // TODO Auto-generated method stub

        if(toast != null)toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showtoastLngthlong(Context context, String message) {

        if(toast != null)toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void cancelToast(){
        if(toast !=null)toast.cancel();
    }

    /***
     * Check Internet Connection
     * Mobile device is connect with Internet or not?
     * ***/
    public static boolean isNetConnected(Context context){
        // TODO Auto-generated method stub

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //This for Wifi.
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected())
        {
            return true;
        }

        //This for Mobile Network.
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected())
        {
            return true;
        }

        //This for Return true else false for Current status.
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
        {
            return true;
        }

        return false;
    }

    /***
     * convert from bitmap to byte array
     * ***/
    public static ArrayList<Integer> getPrimaryColorArray(Context context) {

        ArrayList<Integer> arrylst_prmrycolor = new ArrayList<Integer>();
        try {
            arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.colorPrimaryTeal));
            arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.colorPrimaryBlue));
            arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.colorPrimaryRed));
            arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.colorPrimaryPink));
            arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.colorPrimaryPurple));
            arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.colorPrimaryIndigo));
            arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.colorPrimaryDeepOrange));
            arrylst_prmrycolor.add(ContextCompat.getColor(context, R.color.colorPrimaryBlueGrey));
        } catch (Resources.NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrylst_prmrycolor;
    }

    /***
     * Convert double value to String with 2digit after decimal point
     * ***/
    public static String get2decimalDigitOfDouble(String value){

        String cnvrtd_value = "";

        //cnvrtd_value = new DecimalFormat("#0.00").format(value);
        //cnvrtd_value = String.format("%.2f", value);
//		cnvrtd_value = new DecimalFormat("#0.00").format(double_cnvrtd_value);
//		cnvrtd_value = String.valueOf(checkReplaceCommatoDot(String.valueOf(cnvrtd_value))); // new added

        if(TextUtils.isEmpty(value)) {
            return "";
        } else {

            double double_cnvrtd_value = Math.round(Double.valueOf(value) * 100) / 100.00;
            Locale locale = new Locale("en"/*, "UK"*/);
            DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
            decimalFormat.applyPattern("#0.00");

            cnvrtd_value = decimalFormat.format(double_cnvrtd_value);
        }

        return cnvrtd_value;
    }

    /***
     * Check and convert given Date into 2 digits (from 7 to 07)
     * ***/
    public static String setDateIn2Digit(int number) {
        return number<=9?"0"+number:String.valueOf(number);
    }

    /***
     * Get Current TimeStamp
     * ***/
    public static String getcurrentTimestamp() {

        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /***
     * Open native call dialer with given number
     * ***/
    public static void openDialerToCall(Context context, String mobileNo) {

        try {
            context.startActivity(
                    new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + mobileNo))
            );
        } catch (Exception e) {e.printStackTrace();}
    }
}
