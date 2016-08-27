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
import com.jalotsav.sarvamsugar.adapters.RcyclrDalalwiseSalesAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlDalalWiseSales;
import com.jalotsav.sarvamsugar.model.MdlDalalwsSlsData;
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
 * Created by JALOTSAV Dev. on 27/8/16.
 */
public class FrgmntDalalwiseSales extends Fragment implements AppConstants, View.OnClickListener {

    CoordinatorLayout mCordntrlyotMain;
    ProgressBar mPrgrsbrMain;
    LinearLayout mLnrlyotAppearHere;
    TextView mTvAppearHere;
    RecyclerViewEmptySupport mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RcyclrDalalwiseSalesAdapter mAdapter;
    FloatingActionButton mFabFilters, mFabApply;
    LinearLayout mLnrlyotFilters;
    ImageView mImgvwFltrRemove, mImgvwFltrClose;
    AppCompatAutoCompleteTextView mAppcmptAutocmplttvSlctdFltrVal;
    TextView mTvSlctdFromDt, mTvSlctdToDt;
    LinearLayout mLnrlyotFromDt, mLnrlyotToDt;

    ArrayList<String> mArrylstDalal;
    Calendar mCalndr;
    int mCrntYear, mCrntMonth, mCrntDay;
    String mReqstFromDT, mReqstToDt, mReqstDalal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_dalalwisesales, container, false);

        setHasOptionsMenu(true);

        mCordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_dalalwssls_stock);
        mPrgrsbrMain = (ProgressBar) rootView.findViewById(R.id.prgrsbr_dalalwssls_main);
        mLnrlyotAppearHere = (LinearLayout) rootView.findViewById(R.id.lnrlyot_recyclremptyvw_appearhere);
        mTvAppearHere = (TextView) rootView.findViewById(R.id.tv_recyclremptyvw_appearhere);
        mRecyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.rcyclrvw_dalalwssls);
        mFabFilters = (FloatingActionButton) rootView.findViewById(R.id.fab_dalalwssls_filters);
        mFabApply = (FloatingActionButton) rootView.findViewById(R.id.fab_dalalwssls_apply);
        mLnrlyotFilters = (LinearLayout) rootView.findViewById(R.id.lnrlyot_dalalwssls_filtersvw);
        mImgvwFltrRemove = (ImageView) rootView.findViewById(R.id.imgvw_dalalwssls_fltrremove);
        mImgvwFltrClose = (ImageView) rootView.findViewById(R.id.imgvw_dalalwssls_fltrclose);
        mAppcmptAutocmplttvSlctdFltrVal = (AppCompatAutoCompleteTextView) rootView.findViewById(R.id.apcmptautocmplttv_dalalwssls_slctdfilterval);
        mTvSlctdFromDt = (TextView) rootView.findViewById(R.id.tv_dalalwssls_slctdfromdt);
        mTvSlctdToDt = (TextView) rootView.findViewById(R.id.tv_dalalwssls_slctdtodt);
        mLnrlyotFromDt = (LinearLayout) rootView.findViewById(R.id.lnrlyot_dalalwssls_fromdt);
        mLnrlyotToDt = (LinearLayout) rootView.findViewById(R.id.lnrlyot_dalalwssls_todt);

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

        mTvAppearHere.setText(getString(R.string.dalalwisesales_appear_here));

        mArrylstDalal = new ArrayList<>();
        ArrayList<MdlDalalwsSlsData> arrylstDalalwsSlsData = new ArrayList<>();
        mAdapter = new RcyclrDalalwiseSalesAdapter(getActivity(), arrylstDalalwsSlsData);
        mRecyclerView.setAdapter(mAdapter);

        mCalndr = Calendar.getInstance();
        mCrntYear = mCalndr.get(Calendar.YEAR);
        mCrntMonth = mCalndr.get(Calendar.MONTH);
        mCrntDay = mCalndr.get(Calendar.DAY_OF_MONTH);

        mReqstFromDT = GeneralFuncations.setDateIn2Digit(mCrntDay) + "-" + (GeneralFuncations.setDateIn2Digit(mCrntMonth + 1)) + "-" + mCrntYear; // Format "27-07-2016"
        mReqstToDt = GeneralFuncations.setDateIn2Digit(mCrntDay) + "-" + (GeneralFuncations.setDateIn2Digit(mCrntMonth + 1)) + "-" + mCrntYear;
        mReqstDalal = "";

        // Set filter selected date to TextView
        setFilterSlctdDateTv();

        if (!GeneralFuncations.isNetConnected(getActivity())) {

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
        } else getDalalWiseSalesList();

        return rootView;
    }

    private void getDalalWiseSalesList() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mFabFilters.setVisibility(View.GONE);

        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroAPI apiDalalwsSls = objRetrofit.create(RetroAPI.class);
        Call<MdlDalalWiseSales> callGodownStck = apiDalalwsSls.getDalalwisesales(API_METHOD_GETDSALESSUMM, mReqstFromDT, mReqstToDt, mReqstDalal);
        callGodownStck.enqueue(new Callback<MdlDalalWiseSales>() {
            @Override
            public void onResponse(Call<MdlDalalWiseSales> call, Response<MdlDalalWiseSales> response) {

                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {

                    MdlDalalWiseSales objMdlDalalWiseSales = response.body();
                    String result = objMdlDalalWiseSales.getResult();
                    String message = objMdlDalalWiseSales.getMessage();

                    ArrayList<MdlDalalwsSlsData> arrylstDalalwsSlsData = objMdlDalalWiseSales.getData();

                    try {

                        if (result.equals("0"))
                            showMySnackBar(message);
                        else {

                            for(MdlDalalwsSlsData objMdlGodownStockData : arrylstDalalwsSlsData) {

                                if(!mArrylstDalal.contains(objMdlGodownStockData.getDalal()))
                                    mArrylstDalal.add(objMdlGodownStockData.getDalal());
                            }

                            mAppcmptAutocmplttvSlctdFltrVal.setAdapter(
                                    new ArrayAdapter<> (getActivity(),android.R.layout.simple_list_item_1, mArrylstDalal)
                            );

                            mAdapter.setFilter(arrylstDalalwsSlsData);
                        }
                    } catch (Exception e) {e.printStackTrace();}
                } else
                    showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<MdlDalalWiseSales> call, Throwable t) {

                t.printStackTrace();
                mPrgrsbrMain.setVisibility(View.GONE);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    // Set filter selected date to TextView
    private void setFilterSlctdDateTv() {

        mTvSlctdFromDt.setText(mReqstFromDT);
        mTvSlctdToDt.setText(mReqstToDt);
        mAppcmptAutocmplttvSlctdFltrVal.setText(mReqstDalal);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab_dalalwssls_filters:

                showFiltersView(); // Show Filters View
                break;
            case R.id.fab_dalalwssls_apply:

                mReqstDalal = mAppcmptAutocmplttvSlctdFltrVal.getText().toString().trim();

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else getDalalWiseSalesList();

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_dalalwssls_fltrremove:

                mReqstFromDT = GeneralFuncations.setDateIn2Digit(mCrntDay) + "-" + (GeneralFuncations.setDateIn2Digit(mCrntMonth + 1)) + "-" + mCrntYear; // Format "27-07-2016"
                mReqstToDt = GeneralFuncations.setDateIn2Digit(mCrntDay) + "-" + (GeneralFuncations.setDateIn2Digit(mCrntMonth + 1)) + "-" + mCrntYear;
                mReqstDalal = "";

                // Set filter selected date to TextView
                setFilterSlctdDateTv();

                ArrayList<MdlDalalwsSlsData> arrylstDalalwsSlsData = new ArrayList<>();
                mAdapter.setFilter(arrylstDalalwsSlsData);

                if (!GeneralFuncations.isNetConnected(getActivity())) {

                    // Show SnackBar with given message
                    showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
                } else getDalalWiseSalesList();

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.imgvw_dalalwssls_fltrclose:

                hideFiltersView(); // Hide Filters View
                break;
            case R.id.lnrlyot_dalalwssls_fromdt:

                DatePickerDialog mFromDatePckrDlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month++;
                        mReqstFromDT = GeneralFuncations.setDateIn2Digit(day) + "-" + GeneralFuncations.setDateIn2Digit(month) + "-" + year;
                        mTvSlctdFromDt.setText(mReqstFromDT);
//                        mTvSlctdFromDt.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(year));
                    }
                }, mCrntYear, mCrntMonth, mCrntDay);
                mFromDatePckrDlg.show();
                break;
            case R.id.lnrlyot_dalalwssls_todt:

                DatePickerDialog mToDatePckrDlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month++;
                        mReqstToDt = GeneralFuncations.setDateIn2Digit(day) + "-" + GeneralFuncations.setDateIn2Digit(month) + "-" + year;
                        mTvSlctdToDt.setText(mReqstToDt);
                    }
                }, mCrntYear, mCrntMonth, mCrntDay);
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
