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

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.adapters.RcyclrDalalwiseSaudaAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlDalalWiseSauda;
import com.jalotsav.sarvamsugar.model.MdlDalalwsSaudaData;
import com.jalotsav.sarvamsugar.retrofitapihelper.RetroAPI;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JALOTSAV Dev. on 1/9/16.
 */
public class FrgmntDalalwiseSauda extends Fragment implements AppConstants, View.OnClickListener {

    CoordinatorLayout mCordntrlyotMain;
    ProgressBar mPrgrsbrMain;
    LinearLayout mLnrlyotAppearHere;
    TextView mTvAppearHere, mTvSlctdFromDt, mTvSlctdToDt;
    RecyclerViewEmptySupport mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RcyclrDalalwiseSaudaAdapter mAdapter;
    FloatingActionButton mFabFilters, mFabApply;
    LinearLayout mLnrlyotFilters;
    ImageView mImgvwFltrRemove, mImgvwFltrClose;
    AppCompatAutoCompleteTextView mAppcmptAutocmplttvSlctdFltrVal;
    LinearLayout mLnrlyotFromDt, mLnrlyotToDt;

    ArrayList<String> mArrylstDalal;
    Calendar mCalndr;
    int mCrntYear, mCrntMonth, mCrntDay, mFromYear, mFromMonth, mFromDay, mToYear, mToMonth, mToDay;
    String mReqstFromDT, mReqstToDt, mReqstDalal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_dalalwisesauda, container, false);

        setHasOptionsMenu(true);

        mCordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_dalalwssauda_stock);
        mPrgrsbrMain = (ProgressBar) rootView.findViewById(R.id.prgrsbr_dalalwssauda_main);
        mLnrlyotAppearHere = (LinearLayout) rootView.findViewById(R.id.lnrlyot_recyclremptyvw_appearhere);
        mTvAppearHere = (TextView) rootView.findViewById(R.id.tv_recyclremptyvw_appearhere);
        mRecyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.rcyclrvw_dalalwssauda);
        mFabFilters = (FloatingActionButton) rootView.findViewById(R.id.fab_dalalwssauda_filters);
        mFabApply = (FloatingActionButton) rootView.findViewById(R.id.fab_dalalwssauda_apply);
        mLnrlyotFilters = (LinearLayout) rootView.findViewById(R.id.lnrlyot_dalalwssauda_filtersvw);
        mImgvwFltrRemove = (ImageView) rootView.findViewById(R.id.imgvw_dalalwssauda_fltrremove);
        mImgvwFltrClose = (ImageView) rootView.findViewById(R.id.imgvw_dalalwssauda_fltrclose);
        mAppcmptAutocmplttvSlctdFltrVal = (AppCompatAutoCompleteTextView) rootView.findViewById(R.id.apcmptautocmplttv_dalalwssauda_slctdfilterval);
        mTvSlctdFromDt = (TextView) rootView.findViewById(R.id.tv_dalalwssauda_slctdfromdt);
        mTvSlctdToDt = (TextView) rootView.findViewById(R.id.tv_dalalwssauda_slctdtodt);
        mLnrlyotFromDt = (LinearLayout) rootView.findViewById(R.id.lnrlyot_dalalwssauda_fromdt);
        mLnrlyotToDt = (LinearLayout) rootView.findViewById(R.id.lnrlyot_dalalwssauda_todt);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEmptyView(mLnrlyotAppearHere);

        mFabFilters.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_filter)
                .color(Color.WHITE));

        mFabApply.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_check)
                .color(Color.WHITE));

        mAppcmptAutocmplttvSlctdFltrVal.setThreshold(1);

        mFabFilters.setOnClickListener(this);
        mFabApply.setOnClickListener(this);
        mImgvwFltrRemove.setOnClickListener(this);
        mImgvwFltrClose.setOnClickListener(this);
        mLnrlyotFromDt.setOnClickListener(this);
        mLnrlyotToDt.setOnClickListener(this);

        mTvAppearHere.setText(getString(R.string.dalalwisesauda_appear_here));

        mArrylstDalal = new ArrayList<>();
        ArrayList<MdlDalalwsSaudaData> arrylstdalalwssaudaData = new ArrayList<>();
        mAdapter = new RcyclrDalalwiseSaudaAdapter(getActivity(), arrylstdalalwssaudaData);
        mRecyclerView.setAdapter(mAdapter);

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
        mReqstDalal = "";

        // Set filter selected date to TextView
        setFilterSlctdDateTv();

        if (!GeneralFuncations.isNetConnected(getActivity())) {

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
        } else getDalalWiseSaudaList();

        return rootView;
    }

    // Set filter selected date to TextView
    private void setFilterSlctdDateTv() {

        mTvSlctdFromDt.setText(mReqstFromDT);
        mTvSlctdToDt.setText(mReqstToDt);
        mAppcmptAutocmplttvSlctdFltrVal.setText(mReqstDalal);
    }

    private void getDalalWiseSaudaList() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mFabFilters.setVisibility(View.GONE);

        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroAPI apidalalwssauda = objRetrofit.create(RetroAPI.class);
        Call<MdlDalalWiseSauda> callDSauda = apidalalwssauda.getDalalwisesauda(API_METHOD_GETDSAUDASUMM, mReqstFromDT, mReqstToDt, mReqstDalal);
        callDSauda.enqueue(new Callback<MdlDalalWiseSauda>() {
            @Override
            public void onResponse(Call<MdlDalalWiseSauda> call, Response<MdlDalalWiseSauda> response) {

                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {

                    MdlDalalWiseSauda objMdlDalalWiseSales = response.body();
                    String result = objMdlDalalWiseSales.getResult();
                    String message = objMdlDalalWiseSales.getMessage();

                    ArrayList<MdlDalalwsSaudaData> arrylstdalalwssaudaData = objMdlDalalWiseSales.getData();

                    try {

                        if (result.equals(RESULT_ZERO))
                            showMySnackBar(message);
                        else {

                            for(MdlDalalwsSaudaData objMdlGodownStockData : arrylstdalalwssaudaData) {

                                if(!mArrylstDalal.contains(objMdlGodownStockData.getDalal()))
                                    mArrylstDalal.add(objMdlGodownStockData.getDalal());
                            }

                            mAppcmptAutocmplttvSlctdFltrVal.setAdapter(
                                    new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, mArrylstDalal)
                            );

                            mAdapter.setFilter(arrylstdalalwssaudaData);
                        }
                    } catch (Exception e) {e.printStackTrace();}
                } else
                    showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<MdlDalalWiseSauda> call, Throwable t) {

                t.printStackTrace();
                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab_dalalwssauda_filters:

                showFiltersView(); // Show Filters View
                break;
            case R.id.fab_dalalwssauda_apply:

                mReqstDalal = mAppcmptAutocmplttvSlctdFltrVal.getText().toString().trim();

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else getDalalWiseSaudaList();

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_dalalwssauda_fltrremove:

                mFromDay = mToDay = mCrntDay;
                mFromMonth = mToMonth = mCrntMonth;
                mFromYear = mToYear = mCrntYear;

                mReqstFromDT = GeneralFuncations.setDateIn2Digit(mFromDay)
                        + "-" + GeneralFuncations.setDateIn2Digit(mFromMonth+1)
                        + "-" + mFromYear; // Format "27-07-2016"
                mReqstToDt = GeneralFuncations.setDateIn2Digit(mToDay)
                        + "-" + GeneralFuncations.setDateIn2Digit(mToMonth+1)
                        + "-" + mToYear;

                mReqstDalal = "";

                // Set filter selected date to TextView
                setFilterSlctdDateTv();

                ArrayList<MdlDalalwsSaudaData> arrylstdalalwssaudaData = new ArrayList<>();
                mAdapter.setFilter(arrylstdalalwssaudaData);

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else getDalalWiseSaudaList();

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_dalalwssauda_fltrclose:

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.lnrlyot_dalalwssauda_fromdt:

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
            case R.id.lnrlyot_dalalwssauda_todt:

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

                mImgvwFltrRemove.performClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
