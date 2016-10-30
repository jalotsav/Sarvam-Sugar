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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.adapters.RcyclrOutstandingAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.LogManager;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.common.UserSessionManager;
import com.jalotsav.sarvamsugar.model.MdlOutstanding;
import com.jalotsav.sarvamsugar.model.MdlOutstandingData;
import com.jalotsav.sarvamsugar.retrofitapihelper.RetroAPI;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JALOTSAV Dev. on 20/7/16.
 */
public class FrgmntOutstandingRprt extends Fragment implements AppConstants, View.OnClickListener {

    UserSessionManager mSession;
    CoordinatorLayout mCordntrlyotMain;
    ProgressBar mPrgrsbrMain;
    LinearLayout mLnrlyotAppearHere;
    TextView mTvAppearHere;
    RecyclerViewEmptySupport mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RcyclrOutstandingAdapter mAdapter;
    FloatingActionButton mFabFilters, mFabApply;
    LinearLayout mLnrlyotFilters;
    ImageView mImgvwFltrRemove, mImgvwFltrClose;
    Spinner mSpnrFltrBy, mSpnrDateType, mSpnrSortby;
    AppCompatAutoCompleteTextView mAppcmptAutocmplttvSlctdFltrVal;
    AppCompatButton mAppcmptbtnToDate;

    MdlOutstanding mObjMdlOutstndng;
    ArrayList<MdlOutstandingData> mArrylstOutstndngData;
    ArrayList<String> mArrylstParty, mArrylstDalal, mArrylstArea, mArrylstZone;
    Calendar mCalndr;
    int mCrntYear, mCrntMonth, mCrntDay, mToYear, mToMonth, mToDay;
    String mReqstToDt, mQueryParty, mQueryDalal, mQueryArea, mQueryZone, mReqstType, mReqstSortby;
    boolean isAPICall = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_outstanding_report, container, false);

        setHasOptionsMenu(true);

        mCordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_outstndng);
        mPrgrsbrMain = (ProgressBar) rootView.findViewById(R.id.prgrsbr_outstndng_main);
        mLnrlyotAppearHere = (LinearLayout) rootView.findViewById(R.id.lnrlyot_recyclremptyvw_appearhere);
        mTvAppearHere = (TextView) rootView.findViewById(R.id.tv_recyclremptyvw_appearhere);
        mRecyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.rcyclrvw_outstndng);
        mFabFilters = (FloatingActionButton) rootView.findViewById(R.id.fab_outstndng_filters);
        mFabApply = (FloatingActionButton) rootView.findViewById(R.id.fab_outstndng_apply);
        mLnrlyotFilters = (LinearLayout) rootView.findViewById(R.id.lnrlyot_outstndng_filtersvw);
        mImgvwFltrRemove = (ImageView) rootView.findViewById(R.id.imgvw_outstndng_fltrremove);
        mImgvwFltrClose = (ImageView) rootView.findViewById(R.id.imgvw_outstndng_fltrclose);
        mSpnrFltrBy = (Spinner) rootView.findViewById(R.id.spnr_outstndng_filterby);
        mSpnrDateType = (Spinner) rootView.findViewById(R.id.spnr_outstndng_datetype);
        mSpnrSortby = (Spinner) rootView.findViewById(R.id.spnr_outstndng_sortby);
        mAppcmptAutocmplttvSlctdFltrVal = (AppCompatAutoCompleteTextView) rootView.findViewById(R.id.apcmptautocmplttv_outstndng_slctdfilterval);
        mAppcmptbtnToDate = (AppCompatButton) rootView.findViewById(R.id.appcmptbtn_outstndng_slcttodate);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEmptyView(mLnrlyotAppearHere);

        mSession = new UserSessionManager(getActivity());

        mFabFilters.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_filter)
                .color(Color.WHITE));
        mFabFilters.setVisibility(View.GONE);

        mFabApply.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_check)
                .color(Color.WHITE));

        mFabFilters.setOnClickListener(this);
        mFabApply.setOnClickListener(this);
        mImgvwFltrRemove.setOnClickListener(this);
        mImgvwFltrClose.setOnClickListener(this);
        mAppcmptbtnToDate.setOnClickListener(this);

        mTvAppearHere.setText(getString(R.string.outstndng_appear_here));

        mArrylstOutstndngData = new ArrayList<>();
        mArrylstParty = new ArrayList<>();
        mArrylstDalal = new ArrayList<>();
        mArrylstArea = new ArrayList<>();
        mArrylstZone = new ArrayList<>();

        ArrayList<MdlOutstandingData> arrylstOutstndngData = new ArrayList<>();
        mAdapter = new RcyclrOutstandingAdapter(getActivity(), arrylstOutstndngData);
        mRecyclerView.setAdapter(mAdapter);

        mCalndr = Calendar.getInstance();
        mCrntYear = mToYear = mCalndr.get(Calendar.YEAR);
        mCrntMonth = mToMonth = mCalndr.get(Calendar.MONTH);
        mCrntDay = mToDay = mCalndr.get(Calendar.DAY_OF_MONTH);

        mReqstToDt = GeneralFuncations.setDateIn2Digit(mToDay)
                + "-" + GeneralFuncations.setDateIn2Digit(mToMonth+1)
                + "-" + mToYear; // Format "15-09-2016"
        mAppcmptbtnToDate.setText(getResources().getString(R.string.to_date_val, mReqstToDt));

        mQueryParty = "";
        mQueryDalal = "";
        mQueryArea = "";
        mQueryZone = "";
        mReqstType = "";
        mReqstSortby = "";

        // Check Storage permission before call AsyncTask for data
        isAPICall = false;
        checkStoragePermission();

        mSpnrFltrBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryAmber));

                mAppcmptAutocmplttvSlctdFltrVal.setText("");
                if (position == 0) {
                    mAppcmptAutocmplttvSlctdFltrVal.setVisibility(View.GONE);
//                    mFabApply.setVisibility(View.GONE);
                } else {
                    mAppcmptAutocmplttvSlctdFltrVal.setVisibility(View.VISIBLE);
//                    mFabApply.setVisibility(View.VISIBLE);
                    setAutoCompltTvAdapter(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpnrDateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryAmber));

                if (position == 2) mReqstType = "1";
                else mReqstType = "0";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpnrSortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryAmber));

                if (position == 2) mReqstSortby = "1";
                else mReqstSortby = "0";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return rootView;
    }

    private void checkStoragePermission() {

        try {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                LogManager.printLog(LOGTYPE_INFO, "Permission Granted");
                if (isAPICall)
                    getOutstandingAPI(); // Call API through Retrofit and store response JSON into device storage file
                else
                    new getOutstandingFromFileAsync().execute(); // AsyncTask through get JSON data of API from device storage file
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

    // Call API through Retrofit and store response JSON into device storage file
    private void getOutstandingAPI() {

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

        RetroAPI apiDalalwsSls = objRetrofit.create(RetroAPI.class);
        Call<ResponseBody> callGodownStck = apiDalalwsSls.getOutstanding(
                API_METHOD_GETOUTSTAND, mSession.getUserId(), mSession.getUserType(),
                mReqstToDt, mReqstType, mQueryParty, mQueryDalal, mQueryArea, mQueryZone, mReqstSortby
        );
        callGodownStck.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (mPrgrsbrMain.isShown()) mPrgrsbrMain.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    String[] strArray = new String[1];
                    try {

                        LogManager.printLog(LOGTYPE_INFO, "Reponse is successful: " + response.isSuccessful());

                        strArray[0] = response.body().string();

                        // Create and save API response in device storage in .json file
                        storeJSONDataToStorage(strArray[0]);

                        // AsynTask through get JSON data of API from device storage file
                        new getOutstandingFromFileAsync().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    // Create and save API response in device storage in .json file
    private void storeJSONDataToStorage(String strResponse) {

        try {

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if (!filesDirectory.exists()) filesDirectory.mkdirs();

            File fileJson = new File(filesDirectory, OUTSTANDING_JSON);
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

    // AsynTask through get JSON data of API from device storage file
    public class getOutstandingFromFileAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPrgrsbrMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mObjMdlOutstndng = getJSONDataFromStorage();
            if (mObjMdlOutstndng != null) {

                if (TextUtils.isEmpty(mObjMdlOutstndng.getResult())
                        || mObjMdlOutstndng.getData() == null) {

                    showMySnackBar(getString(R.string.sync_data_msg));
                } else if (mObjMdlOutstndng.getResult().equalsIgnoreCase(RESULT_ONE)) {

                    mArrylstOutstndngData = mObjMdlOutstndng.getData();
                    if (!mArrylstOutstndngData.isEmpty()) {

                        for(MdlOutstandingData objMdlOutstandingData : mArrylstOutstndngData) {

                            if(!mArrylstParty.contains(objMdlOutstandingData.getPname()))
                                mArrylstParty.add(objMdlOutstandingData.getPname());
                            if(!mArrylstDalal.contains(objMdlOutstandingData.getDalalName()))
                                mArrylstDalal.add(objMdlOutstandingData.getDalalName());
                            if(!mArrylstArea.contains(objMdlOutstandingData.getArea()))
                                mArrylstArea.add(objMdlOutstandingData.getArea());
                            if(!mArrylstZone.contains(objMdlOutstandingData.getZone()))
                                mArrylstZone.add(objMdlOutstandingData.getZone());
                        }
                        if(isAdded())
                            setAutoCompltTvAdapter(0);
                    }
                } else showMySnackBar(getString(R.string.there_are_some_prblm));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(isAdded()) {
                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mArrylstOutstndngData.isEmpty()) {
                    showMySnackBar(getResources().getString(R.string.value_records_sml, mArrylstOutstndngData.size()));
                    mFabFilters.setVisibility(View.VISIBLE);
                }
                mAdapter.setFilter(mArrylstOutstndngData);
            }
        }
    }

    // Create and save API response in device storage in .json file
    private MdlOutstanding getJSONDataFromStorage() {

        try {

            mObjMdlOutstndng = new MdlOutstanding();

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if (!filesDirectory.exists()) filesDirectory.mkdirs();

            File fileJson = new File(filesDirectory, OUTSTANDING_JSON);
            if (fileJson.exists()) {

                FileInputStream fIn = new FileInputStream(fileJson);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                myReader.close();

                Gson mGson = new GsonBuilder().create();
                mObjMdlOutstndng = mGson.fromJson(aBuffer, MdlOutstanding.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mObjMdlOutstndng;
    }

    private void setAutoCompltTvAdapter(int spnrSlctdPosition) {

        mAppcmptAutocmplttvSlctdFltrVal.setThreshold(1);

        ArrayAdapter<String> adapterPName = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, mArrylstParty);
        ArrayAdapter<String> adapterDalal = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, mArrylstDalal);
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, mArrylstArea);
        ArrayAdapter<String> adapterZone = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, mArrylstZone);

        switch (spnrSlctdPosition) {
            case 1:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterPName);
                break;
            case 2:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterDalal);
                break;
            case 3:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterArea);
                break;
            case 4:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterZone);
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab_outstndng_filters:

                showFiltersView(); // Show Filters View
                break;
            case R.id.fab_outstndng_apply:

                String slctdFltrVal = mAppcmptAutocmplttvSlctdFltrVal.getText().toString().trim();
                if (!TextUtils.isEmpty(slctdFltrVal)) {

                    switch (mSpnrFltrBy.getSelectedItemPosition()) {
                        case 1:
                            mQueryParty = slctdFltrVal;
                            break;
                        case 2:
                            mQueryDalal = slctdFltrVal;
                            break;
                        case 3:
                            mQueryArea = slctdFltrVal;
                            break;
                        case 4:
                            mQueryZone = slctdFltrVal;
                            break;
                    }
                }

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    mAdapter.setFilter(filters(mArrylstOutstndngData));
                } else getOutstandingAPI();

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_outstndng_fltrremove:

                mToDay = mCrntDay;
                mToMonth = mCrntMonth;
                mToYear = mCrntYear;

                mReqstToDt = GeneralFuncations.setDateIn2Digit(mToDay)
                        + "-" + GeneralFuncations.setDateIn2Digit(mToMonth+1)
                        + "-" + mToYear; // Format "15-09-2016"
                mAppcmptbtnToDate.setText(getResources().getString(R.string.to_date_val, mReqstToDt));

                ArrayList<MdlOutstandingData> arrylstOutstndngData = new ArrayList<>();
                mAdapter.setFilter(arrylstOutstndngData);

                mSpnrFltrBy.setSelection(0);
                mAppcmptAutocmplttvSlctdFltrVal.setText("");
                mQueryParty = "";
                mQueryDalal = "";
                mQueryArea = "";
                mQueryZone = "";
                mReqstType = "";
                mReqstSortby = "";

                // Check Storage permission before call AsyncTask for data
                isAPICall = false;
                checkStoragePermission();

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_outstndng_fltrclose:

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.appcmptbtn_outstndng_slcttodate:

                DatePickerDialog mToDatePckrDlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        mToYear = year;
                        mToMonth = month;
                        mToDay = day;
                        month++;
                        mReqstToDt = GeneralFuncations.setDateIn2Digit(day) + "-" + GeneralFuncations.setDateIn2Digit(month) + "-" + year;
                        mAppcmptbtnToDate.setText(getResources().getString(R.string.to_date_val, mReqstToDt));
                    }
                }, mToYear, mToMonth, mToDay);
                mToDatePckrDlg.show();
                break;
        }
    }

    // Show Filters View
    private void showFiltersView() {

        mFabFilters.setVisibility(View.GONE);
        mLnrlyotFilters.setVisibility(View.VISIBLE);
        mFabApply.setVisibility(View.VISIBLE);
    }

    // Hide Filters View
    private void hideFiltersView() {

        mFabFilters.setVisibility(View.VISIBLE);
        mLnrlyotFilters.setVisibility(View.GONE);
        mFabApply.setVisibility(View.GONE);
    }

    private ArrayList<MdlOutstandingData> filters(ArrayList<MdlOutstandingData> arrylstMdlOutstandingData) {

        ArrayList<MdlOutstandingData> fltrdOutstandingData = new ArrayList<>();
        for (MdlOutstandingData objMdlOutstandingData : arrylstMdlOutstandingData) {

            String targetParty = objMdlOutstandingData.getPname().toLowerCase();
            String targetDalal = objMdlOutstandingData.getDalalName().toLowerCase();
            String targetArea = objMdlOutstandingData.getArea().toLowerCase();
            String targetZone = objMdlOutstandingData.getZone().toLowerCase();

            if (targetParty.contains(mQueryParty.toLowerCase())
                    && targetDalal.contains(mQueryDalal.toLowerCase())
                    && targetArea.contains(mQueryArea.toLowerCase())
                    && targetZone.contains(mQueryZone.toLowerCase())) {

                fltrdOutstandingData.add(objMdlOutstandingData);
            }
        }

        return fltrdOutstandingData;
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
