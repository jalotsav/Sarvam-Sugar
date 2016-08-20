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
import com.jalotsav.sarvamsugar.common.DatabaseHandler;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.LogManager;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlMasterDtls;
import com.jalotsav.sarvamsugar.model.MdlMasterDtlsData;
import com.jalotsav.sarvamsugar.retrofitapihelper.APIMasterDtls;
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

    DatabaseHandler dbHndlr;

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

    MdlMasterDtls objMdlMasterDtls;
    ArrayList<MdlMasterDtlsData> arrylstMasterDtlsData;
    ArrayList<String> arrylstPartyCode, arrylstPartyName, arrylstDalal, arrylstItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_allmasterdtls, container, false);

        setHasOptionsMenu(true);

        dbHndlr = new DatabaseHandler(getActivity());

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

        arrylstMasterDtlsData = new ArrayList<>();
        arrylstPartyCode = new ArrayList<>();
        arrylstPartyName = new ArrayList<>();
        arrylstDalal = new ArrayList<>();
        arrylstItem = new ArrayList<>();

        // AsynTask through get JSON data of API from device storage file
        new getAllMasterDtlsFromFileAsync().execute();

        mSpnrFltrBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if(position == 0) {
                    mAppcmptAutocmplttvSlctdFltrVal.setText("");
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

                    mAdapter.setFilter(
                            filters(arrylstMasterDtlsData, mSpnrFltrBy.getSelectedItemPosition(), slctdFltrVal)
                    );
                } else
                    showMySnackBar(getString(R.string.enter_atleast_2char_fltr));
                break;
            case R.id.imgvw_allmasterdtls_fltrremove:

                if(!arrylstMasterDtlsData.isEmpty())
                    mAdapter.setFilter(arrylstMasterDtlsData);

                mSpnrFltrBy.setSelection(0);
                mAppcmptAutocmplttvSlctdFltrVal.setText("");

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

            objMdlMasterDtls = getJSONDataFromStorage();
            if(objMdlMasterDtls != null) {

                if(TextUtils.isEmpty(objMdlMasterDtls.getResult())
                        || objMdlMasterDtls.getData() == null) {

                    showMySnackBar(getString(R.string.sync_data_msg));
                } else if(objMdlMasterDtls.getResult().equalsIgnoreCase(RESULT_ONE)) {

                    arrylstMasterDtlsData = objMdlMasterDtls.getData();
                    if(!arrylstMasterDtlsData.isEmpty()) {
                        for(MdlMasterDtlsData objMdlMasterDtlsData : arrylstMasterDtlsData) {
                            arrylstPartyCode.add(objMdlMasterDtlsData.getPcode());
                            arrylstPartyName.add(objMdlMasterDtlsData.getPname());
                            arrylstDalal.add(objMdlMasterDtlsData.getDalal());
                            arrylstItem.add(objMdlMasterDtlsData.getBori());
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
            if(!arrylstMasterDtlsData.isEmpty()) {
                showMySnackBar(getResources().getString(R.string.value_records_sml, arrylstMasterDtlsData.size()));
                mFabFilters.setVisibility(View.VISIBLE);
            }
            mAdapter = new RcyclrAllMasterDtlsAdapter(getActivity(), arrylstMasterDtlsData);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void setAutoCompltTvAdapter(int spnrSlctdPosition) {

        mAppcmptAutocmplttvSlctdFltrVal.setThreshold(1);

        ArrayAdapter<String> adapterPCode = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, arrylstPartyCode);
        ArrayAdapter<String> adapterPName = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, arrylstPartyName);
        ArrayAdapter<String> adapterDalal = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, arrylstDalal);
        ArrayAdapter<String> adapterItem = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_list_item_1, arrylstItem);

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
                mAppcmptAutocmplttvSlctdFltrVal.setAdapter(adapterItem);
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

        APIMasterDtls apiMasterDtls = objRetrofit.create(APIMasterDtls.class);
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

                if(mPrgrsbrMain.isShown()) mPrgrsbrMain.setVisibility(View.GONE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    // Create and save API response in device storage in .json file
    private void storeJSONDataToStorage(String strResponse) {

        try {

            File filesDirectory = PATH_SARVAMSUGAR_FILES;
            if(!filesDirectory.exists())	filesDirectory.mkdirs();

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

            objMdlMasterDtls = new MdlMasterDtls();

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
                objMdlMasterDtls = mGson.fromJson(aBuffer, MdlMasterDtls.class);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return objMdlMasterDtls;
    }

    private ArrayList<MdlMasterDtlsData> filters(ArrayList<MdlMasterDtlsData> arrylstMdlMasterDtlsData, int slctdFilterPostion, String query){

        query = query.toLowerCase();
        ArrayList<MdlMasterDtlsData> fltrdMdlMasterDtlsData = new ArrayList<>();
        for(MdlMasterDtlsData objMdlMasterDtlsData : arrylstMdlMasterDtlsData) {

            String target="";
            switch (slctdFilterPostion) {
                case 1:
                    target = objMdlMasterDtlsData.getPcode().toLowerCase();
                    break;
                case 2:
                    target = objMdlMasterDtlsData.getPname().toLowerCase();
                    break;
                case 3:
                    target = objMdlMasterDtlsData.getDalal().toLowerCase();
                    break;
                case 4:
                    target = objMdlMasterDtlsData.getBori().toLowerCase();
                    break;
            }
            if(target.contains(query)) {
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
