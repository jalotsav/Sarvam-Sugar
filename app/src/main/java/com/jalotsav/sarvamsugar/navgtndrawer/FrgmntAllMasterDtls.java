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

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.adapters.RcyclrAllMasterDtlsAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.LogManager;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlMasterDtls;
import com.jalotsav.sarvamsugar.model.MdlMasterDtlsData;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JALOTSAV Dev. on 6/8/16.
 */
public class FrgmntAllMasterDtls extends Fragment implements AppConstants, View.OnClickListener {

    CoordinatorLayout mCordntrlyotMain;
    ProgressBar mPrgrsbrMain;
    LinearLayout mLnrlyotAppearHere;
    TextView mTvAppearHere;
    RecyclerViewEmptySupport mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RcyclrAllMasterDtlsAdapter mAdapter;
    FloatingActionButton mFabFilters, mFabApply;
    LinearLayout mLnrlyotFilters;
    ImageView mImgvwFltrRemove, mImgvwFltrClose;
    Spinner mSpnrFltrBy;
    AppCompatAutoCompleteTextView mAppcmptAutocmplttvSlctdFltrVal;

    MdlMasterDtls mObjMdlMasterDtls;
    ArrayList<MdlMasterDtlsData> mArrylstMasterDtlsData;
    ArrayList<String> mArrylstPartyCode, mArrylstPartyName, mArrylstDalal, mArrylstArea, mArrylstMobile, mArrylstPhone;
    String mQueryPcode = "", mQueryPname = "", mQueryDalal = "", mQueryArea = "", mQueryMobile = "", mQueryPhone = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_allmasterdtls, container, false);

        setHasOptionsMenu(true);

        mCordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_allmasterdtls_stock);
        mPrgrsbrMain = (ProgressBar) rootView.findViewById(R.id.prgrsbr_allmasterdtls_main);
        mLnrlyotAppearHere = (LinearLayout) rootView.findViewById(R.id.lnrlyot_recyclremptyvw_appearhere);
        mTvAppearHere = (TextView) rootView.findViewById(R.id.tv_recyclremptyvw_appearhere);
        mRecyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.rcyclrvw_allmasterdtls);
        mFabFilters = (FloatingActionButton) rootView.findViewById(R.id.fab_allmasterdtls_filters);
        mFabApply = (FloatingActionButton) rootView.findViewById(R.id.fab_allmasterdtls_apply);
        mLnrlyotFilters = (LinearLayout) rootView.findViewById(R.id.lnrlyot_allmasterdtls_filtersvw);
        mImgvwFltrRemove = (ImageView) rootView.findViewById(R.id.imgvw_allmasterdtls_fltrremove);
        mImgvwFltrClose = (ImageView) rootView.findViewById(R.id.imgvw_allmasterdtls_fltrclose);
        mSpnrFltrBy = (Spinner) rootView.findViewById(R.id.spnr_allmasterdtls_filterby);
        mAppcmptAutocmplttvSlctdFltrVal = (AppCompatAutoCompleteTextView) rootView.findViewById(R.id.apcmptautocmplttv_allmasterdtls_slctdfilterval);

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

        mTvAppearHere.setText(getString(R.string.masterdtls_appear_here));

        mArrylstMasterDtlsData = new ArrayList<>();
        mArrylstPartyCode = new ArrayList<>();
        mArrylstPartyName = new ArrayList<>();
        mArrylstDalal = new ArrayList<>();
        mArrylstArea = new ArrayList<>();
        mArrylstMobile = new ArrayList<>();
        mArrylstPhone = new ArrayList<>();

        // AsynTask through get JSON data of API from device storage file
        new getAllMasterDtlsFromFileAsync().execute();

        mSpnrFltrBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                ((TextView) adapterView.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryAmber));

                mAppcmptAutocmplttvSlctdFltrVal.setText("");
                if(position == 0) {
                    mAppcmptAutocmplttvSlctdFltrVal.setVisibility(View.GONE);
                    mFabApply.setVisibility(View.GONE);
                } else {
                    mAppcmptAutocmplttvSlctdFltrVal.setVisibility(View.VISIBLE);
                    mFabApply.setVisibility(View.VISIBLE);
                    setAutoCompltTvAdapter(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab_allmasterdtls_filters:

                showFiltersView(); // Show Filters View
                break;
            case R.id.fab_allmasterdtls_apply:

                String slctdFltrVal = mAppcmptAutocmplttvSlctdFltrVal.getText().toString().trim();
                if(!TextUtils.isEmpty(slctdFltrVal)) {

                    switch (mSpnrFltrBy.getSelectedItemPosition()) {
                        case 1:
                            mQueryPcode = slctdFltrVal;
                            break;
                        case 2:
                            mQueryPname = slctdFltrVal;
                            break;
                        case 3:
                            mQueryDalal = slctdFltrVal;
                            break;
                        case 4:
                            mQueryArea = slctdFltrVal;
                            break;
                        case 5:
                            mQueryMobile = slctdFltrVal;
                            break;
                        case 6:
                            mQueryPhone = slctdFltrVal;
                            break;
                    }

                    mAdapter.setFilter(filters(mArrylstMasterDtlsData));

                    hideFiltersView(); // Hide Filters View
                } else
                    showMySnackBar(getString(R.string.enter_atleast_1char_fltr));
                break;
            case R.id.imgvw_allmasterdtls_fltrremove:

                if(!mArrylstMasterDtlsData.isEmpty())
                    mAdapter.setFilter(mArrylstMasterDtlsData);

                mSpnrFltrBy.setSelection(0);
                mAppcmptAutocmplttvSlctdFltrVal.setText("");
                mQueryPcode = "";
                mQueryPname = "";
                mQueryDalal = "";
                mQueryArea = "";
                mQueryMobile = "";
                mQueryPhone = "";

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_allmasterdtls_fltrclose:

                hideFiltersView(); // Hide Filters View
                break;
        }
    }

    // Show Filters View
    private void showFiltersView() {

        mFabFilters.setVisibility(View.GONE);
        mLnrlyotFilters.setVisibility(View.VISIBLE);
        if(mSpnrFltrBy.getSelectedItemPosition() != 0)
            mFabApply.setVisibility(View.VISIBLE);
    }

    // Hide Filters View
    private void hideFiltersView() {

        mFabFilters.setVisibility(View.VISIBLE);
        mLnrlyotFilters.setVisibility(View.GONE);
        mFabApply.setVisibility(View.GONE);
    }

    // AsynTask through get JSON data of API from device storage file
    public class getAllMasterDtlsFromFileAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPrgrsbrMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mObjMdlMasterDtls = getJSONDataFromStorage();
            if(mObjMdlMasterDtls != null) {

                if(TextUtils.isEmpty(mObjMdlMasterDtls.getResult())
                        || mObjMdlMasterDtls.getData() == null) {

                    showMySnackBar(getString(R.string.sync_data_msg));
                } else if(mObjMdlMasterDtls.getResult().equalsIgnoreCase(RESULT_ONE)) {

                    mArrylstMasterDtlsData = mObjMdlMasterDtls.getData();
                    if(!mArrylstMasterDtlsData.isEmpty()) {
                        for(MdlMasterDtlsData objMdlMasterDtlsData : mArrylstMasterDtlsData) {
                            if(!mArrylstPartyCode.contains(objMdlMasterDtlsData.getPcode()))
                                mArrylstPartyCode.add(objMdlMasterDtlsData.getPcode());
                            if(!mArrylstPartyName.contains(objMdlMasterDtlsData.getPname()))
                                mArrylstPartyName.add(objMdlMasterDtlsData.getPname());
                            if(!mArrylstDalal.contains(objMdlMasterDtlsData.getDalal()))
                                mArrylstDalal.add(objMdlMasterDtlsData.getDalal());
                            if(!mArrylstArea.contains(objMdlMasterDtlsData.getArea()))
                                mArrylstArea.add(objMdlMasterDtlsData.getArea());
                            if(!mArrylstMobile.contains(objMdlMasterDtlsData.getMobile()))
                                mArrylstMobile.add(objMdlMasterDtlsData.getMobile());
                            if(!mArrylstPhone.contains(objMdlMasterDtlsData.getPhone()))
                                mArrylstPhone.add(objMdlMasterDtlsData.getPhone());
                        }
                        setAutoCompltTvAdapter(0);
                    }
                } else showMySnackBar(getString(R.string.there_are_some_prblm));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mPrgrsbrMain.setVisibility(View.GONE);
            if(!mArrylstMasterDtlsData.isEmpty()) {
                showMySnackBar(getResources().getString(R.string.value_records_sml, mArrylstMasterDtlsData.size()));
                mFabFilters.setVisibility(View.VISIBLE);
            }
            mAdapter = new RcyclrAllMasterDtlsAdapter(getActivity(), mArrylstMasterDtlsData);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void setAutoCompltTvAdapter(int spnrSlctdPosition) {

        mAppcmptAutocmplttvSlctdFltrVal.setThreshold(1);

        ArrayAdapter<String> adapterPCode = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, mArrylstPartyCode);
        ArrayAdapter<String> adapterPName = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, mArrylstPartyName);
        ArrayAdapter<String> adapterDalal = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, mArrylstDalal);
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, mArrylstArea);
        ArrayAdapter<String> adapterMobile = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, mArrylstMobile);
        ArrayAdapter<String> adapterPhone = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, mArrylstPhone);

        switch (spnrSlctdPosition) {
            case 1:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterPCode);
                break;
            case 2:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterPName);
                break;
            case 3:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterDalal);
                break;
            case 4:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterArea);
                break;
            case 5:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterMobile);
                break;
            case 6:
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterPhone);
                break;
        }

    }

    // Call API through Retrofit and store response JSON into device storage file
    private void getAllMasterDtlsAPI() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);

        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroAPI apiMasterDtls = objRetrofit.create(RetroAPI.class);
        Call<ResponseBody> callGodownStck = apiMasterDtls.getMasterDtls(API_METHOD_GETMASTERRPT);
        callGodownStck.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(mPrgrsbrMain.isShown()) mPrgrsbrMain.setVisibility(View.GONE);

                if(response.isSuccessful()) {

                    String[] strArray = new String[1];
                    try {

                        LogManager.printLog(LOGTYPE_INFO, "Reponse is successful: " + response.isSuccessful());

                        strArray[0] = response.body().string();

                        // Create and save API response in device storage in .json file
                        storeJSONDataToStorage(strArray[0]);

                        // AsynTask through get JSON data of API from device storage file
                        new getAllMasterDtlsFromFileAsync().execute();
                    } catch (Exception e) { e.printStackTrace(); }
                } else showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                if(mPrgrsbrMain.isShown()) mPrgrsbrMain.setVisibility(View.GONE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    // Create and save API response in device storage in .json file
    private void storeJSONDataToStorage(String strResponse) {

        try {

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if(!filesDirectory.exists()) filesDirectory.mkdirs();

            File fileJson = new File(filesDirectory, ALL_MASTER_DTLS_JSON);
            if(fileJson.exists()) fileJson.delete();
            fileJson.createNewFile();

            FileOutputStream fOut = new FileOutputStream(fileJson);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(strResponse);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Create and save API response in device storage in .json file
    private MdlMasterDtls getJSONDataFromStorage() {

        try {

            mObjMdlMasterDtls = new MdlMasterDtls();

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if(!filesDirectory.exists())	filesDirectory.mkdirs();

            File fileJson = new File(filesDirectory, ALL_MASTER_DTLS_JSON);
            if(fileJson.exists()) {

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
                mObjMdlMasterDtls = mGson.fromJson(aBuffer, MdlMasterDtls.class);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return mObjMdlMasterDtls;
    }

    private ArrayList<MdlMasterDtlsData> filters(ArrayList<MdlMasterDtlsData> arrylstMdlMasterDtlsData){

        ArrayList<MdlMasterDtlsData> fltrdMdlMasterDtlsData = new ArrayList<>();
        for(MdlMasterDtlsData objMdlMasterDtlsData : arrylstMdlMasterDtlsData) {

            String targetPcode = objMdlMasterDtlsData.getPcode().toLowerCase();
            String targetPname = objMdlMasterDtlsData.getPname().toLowerCase();
            String targetDalal = objMdlMasterDtlsData.getDalal().toLowerCase();
            String targetArea = objMdlMasterDtlsData.getArea().toLowerCase();
            String targetMobile = objMdlMasterDtlsData.getMobile().toLowerCase();
            String targetPhone = objMdlMasterDtlsData.getPhone().toLowerCase();

            if(targetPcode.contains(mQueryPcode.toLowerCase())
                    && targetPname.contains(mQueryPname.toLowerCase())
                    && targetDalal.contains(mQueryDalal.toLowerCase())
                    && targetArea.contains(mQueryArea.toLowerCase())
                    && targetMobile.contains(mQueryMobile.toLowerCase())
                    && targetPhone.contains(mQueryPhone.toLowerCase())) {

                fltrdMdlMasterDtlsData.add(objMdlMasterDtlsData);
            }
        }

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
                } else getAllMasterDtlsAPI(); // Call API through Retrofit and store response JSON into device storage file
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
