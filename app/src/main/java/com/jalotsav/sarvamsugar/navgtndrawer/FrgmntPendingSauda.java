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

import android.app.DatePickerDialog;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.adapters.RcyclrPendngSaudaAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlPendngSauda;
import com.jalotsav.sarvamsugar.model.MdlPendngSaudaData;
import com.jalotsav.sarvamsugar.retrofitapihelper.RetroAPI;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

    ArrayList<String> mArrylstParty, mArrylstDalal, mArrylstItem, mArrylstArea;
    Calendar mCalndr;
    int mCrntYear, mCrntMonth, mCrntDay, mFromYear, mFromMonth, mFromDay, mToYear, mToMonth, mToDay;
    String mReqstFromDt, mReqstToDt, mReqstParty, mReqstDalal, mReqstItem, mReqstArea;

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

        mReqstParty = "";
        mReqstDalal = "";
        mReqstItem = "";
        mReqstArea = "";

        // Set filter selected date to TextView
        setFilterSlctdDateTv();

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

        if (!GeneralFuncations.isNetConnected(getActivity())) {

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
        } else getOutstandingList();

        return rootView;
    }

    // Set filter selected date to TextView
    private void setFilterSlctdDateTv() {

        mTvSlctdFromDt.setText(mReqstFromDt);
        mTvSlctdToDt.setText(mReqstToDt);
    }

    private void getOutstandingList() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mFabFilters.setVisibility(View.GONE);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(2, TimeUnit.MINUTES);
        clientBuilder.readTimeout(2, TimeUnit.MINUTES);
        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroAPI apiDalalwsSls = objRetrofit.create(RetroAPI.class);
        Call<MdlPendngSauda> callGodownStck = apiDalalwsSls.getPendingSauda(
                API_METHOD_GETPSAUDA, mReqstFromDt, mReqstToDt, mReqstParty, mReqstDalal, mReqstItem, mReqstArea
        );
        callGodownStck.enqueue(new Callback<MdlPendngSauda>() {
            @Override
            public void onResponse(Call<MdlPendngSauda> call, Response<MdlPendngSauda> response) {

                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {

                    MdlPendngSauda objMdlOutstanding = response.body();
                    String result = objMdlOutstanding.getResult();
                    String message = objMdlOutstanding.getMessage();

                    ArrayList<MdlPendngSaudaData> arrylstOutstndngData = objMdlOutstanding.getData();

                    try {

                        if (result.equals("0"))
                            showMySnackBar(message);
                        else {

                            for(MdlPendngSaudaData objMdlGodownStockData : arrylstOutstndngData) {

                                if(!mArrylstParty.contains(objMdlGodownStockData.getPname()))
                                    mArrylstParty.add(objMdlGodownStockData.getPname());
                                if(!mArrylstDalal.contains(objMdlGodownStockData.getDalal()))
                                    mArrylstDalal.add(objMdlGodownStockData.getDalal());
                                if(!mArrylstItem.contains(objMdlGodownStockData.getItem()))
                                    mArrylstItem.add(objMdlGodownStockData.getItem());
                                if(!mArrylstArea.contains(objMdlGodownStockData.getArea()))
                                    mArrylstArea.add(objMdlGodownStockData.getArea());
                            }
                            setAutoCompltTvAdapter(0);

                            mAdapter.setFilter(arrylstOutstndngData);
                        }
                    } catch (Exception e) {e.printStackTrace();}
                } else
                    showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<MdlPendngSauda> call, Throwable t) {

                t.printStackTrace();
                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
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
                            mReqstParty = slctdFltrVal;
                            break;
                        case 2:
                            mReqstDalal = slctdFltrVal;
                            break;
                        case 3:
                            mReqstArea = slctdFltrVal;
                            break;
                        case 4:
                            mReqstItem= slctdFltrVal;
                            break;
                    }
                } /*else
                    showMySnackBar(getString(R.string.enter_atleast_1char_fltr));*/

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else getOutstandingList();

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
                mReqstParty = "";
                mReqstDalal = "";
                mReqstItem = "";
                mReqstArea = "";

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else getOutstandingList();

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
                } else getOutstandingList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
