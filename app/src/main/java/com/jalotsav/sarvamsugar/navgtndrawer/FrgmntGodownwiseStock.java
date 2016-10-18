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

package com.jalotsav.sarvamsugar.navgtndrawer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.adapters.RcyclrGodownwiseStockAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.LogManager;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlAllGodowns;
import com.jalotsav.sarvamsugar.model.MdlGodownStock;
import com.jalotsav.sarvamsugar.model.MdlGodownStockData;
import com.jalotsav.sarvamsugar.retrofitapihelper.RetroAPI;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JALOTSAV Dev. on 24/7/16.
 */
public class FrgmntGodownwiseStock extends Fragment implements AppConstants, View.OnClickListener {

    CoordinatorLayout mCordntrlyotMain;
    ProgressBar mPrgrsbrMain;
    LinearLayout mLnrlyotAppearHere;
    TextView mTvAppearHere, mTvSlctdFromDt, mTvSlctdToDt;
    RecyclerViewEmptySupport mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RcyclrGodownwiseStockAdapter mAdapter;
    FloatingActionButton mFabFilters;
    BottomSheetDialog mBottomSheetDialog;
    ImageView mImgvwFltrRemove, mImgvwFltrApply;
    LinearLayout mLnrlyotFromDt, mLnrlyotToDt;

    Calendar mCalndr;
    int mCrntYear, mCrntMonth, mCrntDay, mFromYear, mFromMonth, mFromDay, mToYear, mToMonth, mToDay;
    String mReqstFromDT, mReqstToDt;
    ArrayList<MdlGodownStockData> mArrylstGodownStckData;
    MdlGodownStockData mObjMdlGodownStockData;
    MdlGodownStock mObjMdlGodownStock;
    boolean isAPICall = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_godownwise_stock, container, false);

        setHasOptionsMenu(true);

        mCordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_godownwise_stock);
        mPrgrsbrMain = (ProgressBar) rootView.findViewById(R.id.prgrsbr_godownwise_stock_main);
        mLnrlyotAppearHere = (LinearLayout) rootView.findViewById(R.id.lnrlyot_recyclremptyvw_appearhere);
        mTvAppearHere = (TextView) rootView.findViewById(R.id.tv_recyclremptyvw_appearhere);
        mRecyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.rcyclrvw_godownwise_stock);
        mFabFilters = (FloatingActionButton) rootView.findViewById(R.id.fab_godownwise_stock_filters);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEmptyView(mLnrlyotAppearHere);

        mFabFilters.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_filter)
                .color(Color.WHITE));
        mFabFilters.setVisibility(View.GONE);

        mArrylstGodownStckData = new ArrayList<>();

        // Check Storage permission before call AsyncTask for data
        isAPICall = false;
        checkStoragePermission();

        mBottomSheetDialog = new BottomSheetDialog(getActivity());

        mCalndr = Calendar.getInstance();
        mCrntYear = mFromYear = mToYear = mCalndr.get(Calendar.YEAR);
        mCrntMonth = mFromMonth = mToMonth = mCalndr.get(Calendar.MONTH);
        mCrntDay = mFromDay = mToDay = mCalndr.get(Calendar.DAY_OF_MONTH);

        mReqstFromDT = GeneralFuncations.setDateIn2Digit(mFromDay)
                + "-" + GeneralFuncations.setDateIn2Digit(mFromMonth+1)
                + "-" + mFromYear; // Format "27-07-2016"
        mReqstToDt = GeneralFuncations.setDateIn2Digit(mToDay)
                + "-" + GeneralFuncations.setDateIn2Digit(mToMonth+1)
                + "-" + mToYear;

        mFabFilters.setOnClickListener(this);

        mTvAppearHere.setText(getString(R.string.godownwisestck_appear_here));

        return rootView;
    }

    private void checkStoragePermission() {

        try {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                LogManager.printLog(LOGTYPE_INFO, "Permission Granted");
                if (isAPICall)
                    getGodownStockAPI(); // Call API through Retrofit and store response JSON into device storage file
                else
                    new getGodownStockFromFileAsync().execute(); // AsyncTask through get JSON data of API from device storage file
            } else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
                    GeneralFuncations.showtoastLngthlong(getActivity(), getString(R.string.you_must_allow_permsn));

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMSN_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMSN_STORAGE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                checkStoragePermission();
            else
                showMySnackBar(getString(R.string.permsn_denied));
        }
    }

    // AsynTask through get JSON data of API from device storage file
    public class getGodownStockFromFileAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPrgrsbrMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mArrylstGodownStckData = new ArrayList<>();

            try {

                String websrvcRespns = getJSONDataFromStorage();
                if(TextUtils.isEmpty(websrvcRespns))
                    showMySnackBar(getString(R.string.sync_data_msg));
                else {
                    JSONObject jsnObj = new JSONObject(websrvcRespns);
                    String result = jsnObj.getString("result");
                    String message = jsnObj.getString("message");
                    if (result.equals(RESULT_ZERO))
                        showMySnackBar(message);
                    else {

                        JSONArray jsnArrayData = jsnObj.getJSONArray("data");
                        for (int i = 0; i < jsnArrayData.length(); i++) {

                            mObjMdlGodownStockData = new MdlGodownStockData();
                            ArrayList<MdlAllGodowns> arrylstMdlAllGodowns = new ArrayList<>();

                            JSONObject jsonobjData = jsnArrayData.getJSONObject(i);
                            mObjMdlGodownStockData.setItemName(jsonobjData.getString("Item Name"));
                            mObjMdlGodownStockData.setPacking(jsonobjData.getString("Packing"));
                            mObjMdlGodownStockData.setTotalStock(jsonobjData.getString("Total Stk."));
                            mObjMdlGodownStockData.setPendingSauda(jsonobjData.getString("Pend.Sauda"));
                            mObjMdlGodownStockData.setNetStock(jsonobjData.getString("Net Stk."));
                            mObjMdlGodownStockData.setStkBori(jsonobjData.getString("Stk.Bori"));
                            JSONArray jsnArryGoDowns = jsonobjData.getJSONArray("goDowns");

                            for (int j = 0; j < jsnArryGoDowns.length(); j++) {

                                JSONObject jsnGoDowns = jsnArryGoDowns.getJSONObject(j);

                                Iterator<String> iter = jsnGoDowns.keys();
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    Object value = jsnGoDowns.get(key);

                                    MdlAllGodowns objMdlAllGodowns = new MdlAllGodowns(key, value.toString());
                                    arrylstMdlAllGodowns.add(objMdlAllGodowns);
                                }

                                mObjMdlGodownStockData.setArrylstAllGodowns(arrylstMdlAllGodowns);

                                mArrylstGodownStckData.add(mObjMdlGodownStockData);
                            }
                        }
                    }
                }
            } catch (Exception e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(isAdded()) {
                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mArrylstGodownStckData.isEmpty()) {
                    showMySnackBar(getResources().getString(R.string.value_records_sml, mArrylstGodownStckData.size()));
                    mFabFilters.setVisibility(View.VISIBLE);
                }
                mAdapter = new RcyclrGodownwiseStockAdapter(getActivity(), mArrylstGodownStckData);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    private void getGodownStockAPI() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mFabFilters.setVisibility(View.GONE);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(3, TimeUnit.MINUTES);
        clientBuilder.readTimeout(3, TimeUnit.MINUTES);
        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroAPI apiGodownStck = objRetrofit.create(RetroAPI.class);
        Call<ResponseBody> callGodownStck = apiGodownStck.getGodownStock(API_METHOD_GETGODOWNSTK, mReqstFromDT, mReqstToDt);
        callGodownStck.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (mPrgrsbrMain.isShown()) mPrgrsbrMain.setVisibility(View.GONE);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {

                    String[] strArray = new String[1];
                    try {

                        LogManager.printLog(LOGTYPE_INFO, "Reponse is successful: " + response.isSuccessful());

                        strArray[0] = response.body().string();

                        // Create and save API response in device storage in .json file
                        storeJSONDataToStorage(strArray[0]);

                        // AsynTask through get JSON data of API from device storage file
                        new getGodownStockFromFileAsync().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                if (mPrgrsbrMain.isShown()) mPrgrsbrMain.setVisibility(View.GONE);
                if (mFabFilters.isShown()) mFabFilters.setVisibility(View.GONE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    // Create and save API response in device storage in .json file
    private void storeJSONDataToStorage(String strResponse) {

        try {

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if (!filesDirectory.exists()) filesDirectory.mkdirs();

            File fileJson = new File(filesDirectory, GODOWN_WISE_STOCK_JSON);
            if (fileJson.exists()) fileJson.delete();
            fileJson.createNewFile();

            FileOutputStream fOut = new FileOutputStream(fileJson);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(strResponse);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create and save API response in device storage in .json file
    private String getJSONDataFromStorage() {

        String aDataRow = "";
        String aBuffer = "";
        try {

            mObjMdlGodownStock = new MdlGodownStock();

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if (!filesDirectory.exists()) filesDirectory.mkdirs();

            File fileJson = new File(filesDirectory, GODOWN_WISE_STOCK_JSON);
            if (fileJson.exists()) {

                FileInputStream fIn = new FileInputStream(fileJson);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                myReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return aBuffer;
    }

    private void showFiltersBottomSheets() {

        View contnVw = getActivity().getLayoutInflater().inflate(R.layout.lo_bottomsheets_fromtodate, null);
        mImgvwFltrRemove = (ImageView) contnVw.findViewById(R.id.imgvw_btmshts_frmtodt_fltrremove);
        mImgvwFltrApply = (ImageView) contnVw.findViewById(R.id.imgvw_btmshts_frmtodt_fltrapply);
        mTvSlctdFromDt = (TextView) contnVw.findViewById(R.id.tv_btmshts_frmtodt_slctdfromdt);
        mTvSlctdToDt = (TextView) contnVw.findViewById(R.id.tv_btmshts_frmtodt_slctdtodt);
        mLnrlyotFromDt = (LinearLayout) contnVw.findViewById(R.id.lnrlyot_btmshts_frmtodt_fromdt);
        mLnrlyotToDt = (LinearLayout) contnVw.findViewById(R.id.lnrlyot_btmshts_frmtodt_todt);

        // Set filter selected date to TextView
        setFilterSlctdDateTv();

        mBottomSheetDialog.setContentView(contnVw);
        mBottomSheetDialog.show();

        mImgvwFltrRemove.setOnClickListener(this);
        mImgvwFltrApply.setOnClickListener(this);
        mLnrlyotFromDt.setOnClickListener(this);
        mLnrlyotToDt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_godownwise_stock_filters:

                showFiltersBottomSheets();
                break;
            case R.id.imgvw_btmshts_frmtodt_fltrapply:

                mBottomSheetDialog.dismiss();

                mAdapter.setFilter(filters(mArrylstGodownStckData));

//                onRefresh();
                break;
            case R.id.imgvw_btmshts_frmtodt_fltrremove:

                mFromDay = mToDay = mCrntDay;
                mFromMonth = mToMonth = mCrntMonth;
                mFromYear = mToYear = mCrntYear;

                mReqstFromDT = GeneralFuncations.setDateIn2Digit(mFromDay)
                        + "-" + GeneralFuncations.setDateIn2Digit(mFromMonth+1)
                        + "-" + mFromYear; // Format "27-07-2016"
                mReqstToDt = GeneralFuncations.setDateIn2Digit(mToDay)
                        + "-" + GeneralFuncations.setDateIn2Digit(mToMonth+1)
                        + "-" + mToYear;

                ArrayList<MdlGodownStockData> arrylstGodownStckData = new ArrayList<>();
                mAdapter.setFilter(arrylstGodownStckData);

                mBottomSheetDialog.dismiss();
                break;
            case R.id.lnrlyot_btmshts_frmtodt_fromdt:

                DatePickerDialog mFromDatePckrDlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        mFromYear = year;
                        mFromMonth = month;
                        mFromDay = day;
                        month++;
                        mReqstFromDT = GeneralFuncations.setDateIn2Digit(day) + "-" + GeneralFuncations.setDateIn2Digit(month) + "-" + year;
                        mTvSlctdFromDt.setText(mReqstFromDT);
//                        mTvSlctdFromDt.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(year));
                    }
                }, mFromYear, mFromMonth, mFromDay);
                mFromDatePckrDlg.show();
                break;
            case R.id.lnrlyot_btmshts_frmtodt_todt:

                DatePickerDialog mToDatePckrDlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        mToYear = year;
                        mToMonth = month;
                        mToDay = day;
                        month++;
                        mReqstToDt = GeneralFuncations.setDateIn2Digit(day) + "-" + GeneralFuncations.setDateIn2Digit(month) + "-" + year;
                        mTvSlctdToDt.setText(mReqstToDt);
                    }
                }, mToYear, mToMonth, mToDay);
                mToDatePckrDlg.show();
                break;
        }
    }

    // Set filter selected date to TextView
    private void setFilterSlctdDateTv() {

        mTvSlctdFromDt.setText(mReqstFromDT);
        mTvSlctdToDt.setText(mReqstToDt);
    }

    private ArrayList<MdlGodownStockData> filters(ArrayList<MdlGodownStockData> arrylstMdlMasterDtlsData) {

        ArrayList<MdlGodownStockData> fltrdMdlMasterDtlsData = new ArrayList<>();
        /*for (MdlGodownStockData objMdlMasterDtlsData : arrylstMdlMasterDtlsData) {

            String targetPcode = objMdlMasterDtlsData.getPcode().toLowerCase();
            String targetPname = objMdlMasterDtlsData.getPname().toLowerCase();

            if (targetPcode.contains(mQueryPcode.toLowerCase())
                    && targetPname.contains(mQueryPname.toLowerCase())
                    && targetDalal.contains(mQueryDalal.toLowerCase())
                    && targetArea.contains(mQueryArea.toLowerCase())
                    && targetMobile.contains(mQueryMobile.toLowerCase())
                    && targetPhone.contains(mQueryPhone.toLowerCase())) {

                fltrdMdlMasterDtlsData.add(objMdlMasterDtlsData);
            }
        }*/

        return fltrdMdlMasterDtlsData;
    }

    // Show SnackBar with given message
    private void showMySnackBar(String message) {

        Snackbar.make(mCordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else {
                    isAPICall = true;
                    checkStoragePermission();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
