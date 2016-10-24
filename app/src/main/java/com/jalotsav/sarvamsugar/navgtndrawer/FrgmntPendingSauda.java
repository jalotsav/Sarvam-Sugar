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
import com.jalotsav.sarvamsugar.adapters.RcyclrPendngSaudaAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.LogManager;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlPendngSauda;
import com.jalotsav.sarvamsugar.model.MdlPendngSaudaData;
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
 * Created by JALOTSAV Dev. on 18/9/16.
 */
public class FrgmntPendingSauda extends Fragment implements AppConstants, View.OnClickListener {

    CoordinatorLayout mCordntrlyotMain;
    ProgressBar mPrgrsbrMain;
    LinearLayout mLnrlyotAppearHere;
    TextView mTvAppearHere, mTvSlctdFromDt, mTvSlctdToDt;
    RecyclerViewEmptySupport mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RcyclrPendngSaudaAdapter mAdapter;
    FloatingActionButton mFabFilters, mFabApply;
    LinearLayout mLnrlyotFilters;
    ImageView mImgvwFltrRemove, mImgvwFltrClose;
    Spinner mSpnrFltrBy;
    AppCompatAutoCompleteTextView mAppcmptAutocmplttvSlctdFltrVal;
    LinearLayout mLnrlyotFromDt, mLnrlyotToDt;

    MdlPendngSauda mObjMdlPndngSauda;
    ArrayList<MdlPendngSaudaData> mArrylstPndngSaudaData;
    ArrayList<String> mArrylstParty, mArrylstDalal, mArrylstItem, mArrylstArea;
    Calendar mCalndr;
    int mCrntYear, mCrntMonth, mCrntDay, mFromYear, mFromMonth, mFromDay, mToYear, mToMonth, mToDay;
    String mReqstFromDt, mReqstToDt,
            mQueryParty = "", mQueryDalal = "", mQueryItem = "", mQueryArea = "";
    boolean isAPICall = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_pendngsauda, container, false);

        setHasOptionsMenu(true);

        mCordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_pendngsauda);
        mPrgrsbrMain = (ProgressBar) rootView.findViewById(R.id.prgrsbr_pendngsauda_main);
        mLnrlyotAppearHere = (LinearLayout) rootView.findViewById(R.id.lnrlyot_recyclremptyvw_appearhere);
        mTvAppearHere = (TextView) rootView.findViewById(R.id.tv_recyclremptyvw_appearhere);
        mTvSlctdFromDt = (TextView) rootView.findViewById(R.id.tv_pendngsauda_slctdfromdt);
        mTvSlctdToDt = (TextView) rootView.findViewById(R.id.tv_pendngsauda_slctdtodt);
        mRecyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.rcyclrvw_pendngsauda);
        mFabFilters = (FloatingActionButton) rootView.findViewById(R.id.fab_pendngsauda_filters);
        mFabApply = (FloatingActionButton) rootView.findViewById(R.id.fab_pendngsauda_apply);
        mLnrlyotFilters = (LinearLayout) rootView.findViewById(R.id.lnrlyot_pendngsauda_filtersvw);
        mImgvwFltrRemove = (ImageView) rootView.findViewById(R.id.imgvw_pendngsauda_fltrremove);
        mImgvwFltrClose = (ImageView) rootView.findViewById(R.id.imgvw_pendngsauda_fltrclose);
        mSpnrFltrBy = (Spinner) rootView.findViewById(R.id.spnr_pendngsauda_filterby);
        mAppcmptAutocmplttvSlctdFltrVal = (AppCompatAutoCompleteTextView) rootView.findViewById(R.id.apcmptautocmplttv_pendngsauda_slctdfilterval);
        mLnrlyotFromDt = (LinearLayout) rootView.findViewById(R.id.lnrlyot_pendngsauda_fromdt);
        mLnrlyotToDt = (LinearLayout) rootView.findViewById(R.id.lnrlyot_pendngsauda_todt);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEmptyView(mLnrlyotAppearHere);

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
        mLnrlyotFromDt.setOnClickListener(this);
        mLnrlyotToDt.setOnClickListener(this);

        mTvAppearHere.setText(getString(R.string.pendngsauda_appear_here));

        mArrylstPndngSaudaData = new ArrayList<>();
        mArrylstParty = new ArrayList<>();
        mArrylstDalal = new ArrayList<>();
        mArrylstItem = new ArrayList<>();
        mArrylstArea = new ArrayList<>();

        ArrayList<MdlPendngSaudaData> arrylstpendngsaudaData = new ArrayList<>();
        mAdapter = new RcyclrPendngSaudaAdapter(getActivity(), arrylstpendngsaudaData);
        mRecyclerView.setAdapter(mAdapter);

        mCalndr = Calendar.getInstance();
        mCrntYear = mFromYear = mToYear = mCalndr.get(Calendar.YEAR);
        mCrntMonth = mFromMonth = mToMonth = mCalndr.get(Calendar.MONTH);
        mCrntDay = mFromDay = mToDay = mCalndr.get(Calendar.DAY_OF_MONTH);

        mReqstFromDt = GeneralFuncations.setDateIn2Digit(mFromDay)
                + "-" + GeneralFuncations.setDateIn2Digit(mFromMonth+1)
                + "-" + mFromYear; // Format "18-09-2016"
        mReqstToDt = GeneralFuncations.setDateIn2Digit(mToDay)
                + "-" + GeneralFuncations.setDateIn2Digit(mToMonth+1)
                + "-" + mToYear;

        // Set filter selected date to TextView
        setFilterSlctdDateTv();

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

        return rootView;
    }

    // Set filter selected date to TextView
    private void setFilterSlctdDateTv() {

        mTvSlctdFromDt.setText(mReqstFromDt);
        mTvSlctdToDt.setText(mReqstToDt);
    }

    private void checkStoragePermission() {

        try {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                LogManager.printLog(LOGTYPE_INFO, "Permission Granted");
                if (isAPICall)
                    getPendingSaudaAPI(false); // Call API through Retrofit and store response JSON into device storage file
                else
                    new getPendingSaudaFromFileAsync().execute(); // AsyncTask through get JSON data of API from device storage file
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
    private void getPendingSaudaAPI(final boolean isWithFilter) {

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
        Call<ResponseBody> callGodownStck = apiDalalwsSls.getPendingSauda(
                API_METHOD_GETPSAUDA, mReqstFromDt, mReqstToDt, mQueryParty, mQueryDalal, mQueryItem, mQueryArea
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
                        new getPendingSaudaFromFileAsync().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                if (mPrgrsbrMain.isShown()) mPrgrsbrMain.setVisibility(View.GONE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    // Create and save API response in device storage in .json file
    private void storeJSONDataToStorage(String strResponse) {

        try {

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if (!filesDirectory.exists()) filesDirectory.mkdirs();

            File fileJson = new File(filesDirectory, PENDING_SAUDA_JSON);
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
    public class getPendingSaudaFromFileAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPrgrsbrMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mObjMdlPndngSauda = getJSONDataFromStorage();
            if (mObjMdlPndngSauda != null) {

                if (TextUtils.isEmpty(mObjMdlPndngSauda.getResult())
                        || mObjMdlPndngSauda.getData() == null) {

                    showMySnackBar(getString(R.string.sync_data_msg));
                } else if (mObjMdlPndngSauda.getResult().equalsIgnoreCase(RESULT_ONE)) {

                    mArrylstPndngSaudaData = mObjMdlPndngSauda.getData();
                    if (!mArrylstPndngSaudaData.isEmpty()) {

                        for (MdlPendngSaudaData objMdlPndngSaudaData : mArrylstPndngSaudaData) {

                            if(!mArrylstParty.contains(objMdlPndngSaudaData.getPname()))
                                mArrylstParty.add(objMdlPndngSaudaData.getPname());
                            if(!mArrylstDalal.contains(objMdlPndngSaudaData.getDalal()))
                                mArrylstDalal.add(objMdlPndngSaudaData.getDalal());
                            if(!mArrylstItem.contains(objMdlPndngSaudaData.getItem()))
                                mArrylstItem.add(objMdlPndngSaudaData.getItem());
                            if(!mArrylstArea.contains(objMdlPndngSaudaData.getArea()))
                                mArrylstArea.add(objMdlPndngSaudaData.getArea());
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
                if (!mArrylstPndngSaudaData.isEmpty()) {
                    showMySnackBar(getResources().getString(R.string.value_records_sml, mArrylstPndngSaudaData.size()));
                    mFabFilters.setVisibility(View.VISIBLE);
                }
                mAdapter.setFilter(mArrylstPndngSaudaData);
            }
        }
    }

    // Create and save API response in device storage in .json file
    private MdlPendngSauda getJSONDataFromStorage() {

        try {

            mObjMdlPndngSauda = new MdlPendngSauda();

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if (!filesDirectory.exists()) filesDirectory.mkdirs();

            File fileJson = new File(filesDirectory, PENDING_SAUDA_JSON);
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
                mObjMdlPndngSauda = mGson.fromJson(aBuffer, MdlPendngSauda.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mObjMdlPndngSauda;
    }

    private void setAutoCompltTvAdapter(int spnrSlctdPosition) {

        mAppcmptAutocmplttvSlctdFltrVal.setThreshold(1);

        ArrayAdapter<String> adapterPName = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, mArrylstParty);
        ArrayAdapter<String> adapterDalal = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, mArrylstDalal);
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, mArrylstArea);
        ArrayAdapter<String> adapterItem = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, mArrylstItem);

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
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterItem);
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab_pendngsauda_filters:

                showFiltersView(); // Show Filters View
                break;
            case R.id.fab_pendngsauda_apply:

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
                            mQueryItem = slctdFltrVal;
                            break;
                    }
                }

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    mAdapter.setFilter(filters(mArrylstPndngSaudaData));
                } else getPendingSaudaAPI(true);

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_pendngsauda_fltrremove:

                mFromDay = mToDay = mCrntDay;
                mFromMonth = mToMonth = mCrntMonth;
                mFromYear = mToYear = mCrntYear;

                mReqstFromDt = GeneralFuncations.setDateIn2Digit(mFromDay)
                        + "-" + GeneralFuncations.setDateIn2Digit(mFromMonth+1)
                        + "-" + mFromYear; // Format "18-09-2016"
                mReqstToDt = GeneralFuncations.setDateIn2Digit(mToDay)
                        + "-" + GeneralFuncations.setDateIn2Digit(mToMonth+1)
                        + "-" + mToYear;

                // Set filter selected date to TextView
                setFilterSlctdDateTv();

                ArrayList<MdlPendngSaudaData> arrylstOutstndngData = new ArrayList<>();
                mAdapter.setFilter(arrylstOutstndngData);

                mSpnrFltrBy.setSelection(0);
                mAppcmptAutocmplttvSlctdFltrVal.setText("");
                mQueryParty = "";
                mQueryDalal = "";
                mQueryItem = "";
                mQueryArea = "";

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else getPendingSaudaAPI(false);

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_pendngsauda_fltrclose:

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.lnrlyot_pendngsauda_fromdt:

                DatePickerDialog mFromDatePckrDlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        mFromYear = year;
                        mFromMonth = month;
                        mFromDay = day;
                        month++;
                        mReqstFromDt = GeneralFuncations.setDateIn2Digit(day) + "-" + GeneralFuncations.setDateIn2Digit(month) + "-" + year;
                        mTvSlctdFromDt.setText(mReqstFromDt);
                    }
                }, mFromYear, mFromMonth, mFromDay);
                mFromDatePckrDlg.show();
                break;
            case R.id.lnrlyot_pendngsauda_todt:

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

    private ArrayList<MdlPendngSaudaData> filters(ArrayList<MdlPendngSaudaData> arrylstMdlMasterDtlsData) {

        ArrayList<MdlPendngSaudaData> fltrdPndngSaudaData = new ArrayList<>();
        for (MdlPendngSaudaData objMdlPndngSaudaData : arrylstMdlMasterDtlsData) {

            String targetParty = objMdlPndngSaudaData.getPname().toLowerCase();
            String targetDalal = objMdlPndngSaudaData.getDalal().toLowerCase();
            String targetArea = objMdlPndngSaudaData.getArea().toLowerCase();
            String targetItem = objMdlPndngSaudaData.getItem().toLowerCase();

            if (targetParty.contains(mQueryParty.toLowerCase())
                    && targetDalal.contains(mQueryDalal.toLowerCase())
                    && targetArea.contains(mQueryArea.toLowerCase())
                    && targetItem.contains(mQueryItem.toLowerCase())) {

                fltrdPndngSaudaData.add(objMdlPndngSaudaData);
            }
        }

        return fltrdPndngSaudaData;
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
