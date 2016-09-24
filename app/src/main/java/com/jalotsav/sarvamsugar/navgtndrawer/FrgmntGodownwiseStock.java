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
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.adapters.RcyclrGodownwiseStockAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlAllGodowns;
import com.jalotsav.sarvamsugar.model.MdlGodownStockData;
import com.jalotsav.sarvamsugar.retrofitapihelper.RetroAPI;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JALOTSAV Dev. on 24/7/16.
 */
public class FrgmntGodownwiseStock extends Fragment implements AppConstants,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    CoordinatorLayout mCordntrlyotMain;
    SwipeRefreshLayout mSwiperfrshlyot, mSwiperfrshlyotEmptyvw;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_godownwise_stock, container, false);

        mCordntrlyotMain = (CoordinatorLayout) rootView.findViewById(R.id.cordntrlyot_godownwise_stock);
        mSwiperfrshlyot = (SwipeRefreshLayout) rootView.findViewById(R.id.swipwrfrshlyot_godownwise_stock);
        mSwiperfrshlyotEmptyvw = (SwipeRefreshLayout) rootView.findViewById(R.id.swipwrfrshlyot_godownwise_stock_emptyvw);
        mLnrlyotAppearHere = (LinearLayout) rootView.findViewById(R.id.lnrlyot_matches_tabfrgmnt_appearhere);
        mTvAppearHere = (TextView) rootView.findViewById(R.id.tv_matches_tabfrgmnt_appearhere);
        mRecyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.rcyclrvw_godownwise_stock);
        mFabFilters = (FloatingActionButton) rootView.findViewById(R.id.fab_godownwise_stock_filters);

        // Initialization of SwipeRefreshLayout
        initSwipeRefreshLayout(mSwiperfrshlyot);
        initSwipeRefreshLayout(mSwiperfrshlyotEmptyvw);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEmptyView(mSwiperfrshlyotEmptyvw);

        mFabFilters.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_filter)
                .color(Color.WHITE));

        ArrayList<MdlGodownStockData> arrylstGodownStckData = new ArrayList<>();
        mAdapter = new RcyclrGodownwiseStockAdapter(getActivity(), arrylstGodownStckData);
        mRecyclerView.setAdapter(mAdapter);

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

        if (!GeneralFuncations.isNetConnected(getActivity())) {

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
        } else getGodownStockList();

        return rootView;
    }

    // Initialization of SwipeRefreshLayout
    private void initSwipeRefreshLayout(SwipeRefreshLayout swprefrhlyot) {

        swprefrhlyot.setOnRefreshListener(this);
        swprefrhlyot.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryTeal),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryRed),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryBlue),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryAmber));
    }

    @Override
    public void onRefresh() {

        if (!GeneralFuncations.isNetConnected(getActivity())) {

            mSwiperfrshlyot.setRefreshing(false);
            mSwiperfrshlyotEmptyvw.setRefreshing(false);

            // Show SnackBar with given message
            showMySnackBar(getResources().getString(R.string.no_intrnt_cnctn));
        } else getGodownStockList();
    }

    private void getGodownStockList() {

        mSwiperfrshlyot.post(new Runnable() {
            @Override
            public void run() {

                mSwiperfrshlyot.setRefreshing(true);
                mSwiperfrshlyotEmptyvw.setRefreshing(true);
            }
        });
        mFabFilters.setVisibility(View.GONE);

        Retrofit objRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroAPI apiGodownStck = objRetrofit.create(RetroAPI.class);
        Call<ResponseBody> callGodownStck = apiGodownStck.getGodownStock(API_METHOD_GETGODOWNSTK, mReqstFromDT, mReqstToDt);
        callGodownStck.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                mSwiperfrshlyot.setRefreshing(false);
                mSwiperfrshlyotEmptyvw.setRefreshing(false);

                if (response.isSuccessful()) {

                    ArrayList<MdlGodownStockData> arrylstGodownStckData = new ArrayList<>();
                    MdlGodownStockData objMdlGodownStockData;

                    try {

                        String websrvcRespns = response.body().string();

                        JSONObject jsnObj = new JSONObject(websrvcRespns);
                        String result = jsnObj.getString("result");
                        String message = jsnObj.getString("message");
                        if (result.equals(RESULT_ZERO))
                            showMySnackBar(message);
                        else {

                            if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);

                            JSONArray jsnArrayData = jsnObj.getJSONArray("data");
                            for (int i = 0; i < jsnArrayData.length(); i++) {

                                objMdlGodownStockData = new MdlGodownStockData();
                                ArrayList<MdlAllGodowns> arrylstMdlAllGodowns = new ArrayList<>();

                                JSONObject jsonobjData = jsnArrayData.getJSONObject(i);
                                objMdlGodownStockData.setItemName(jsonobjData.getString("Item Name"));
                                objMdlGodownStockData.setPacking(jsonobjData.getString("Packing"));
                                objMdlGodownStockData.setTotalStock(jsonobjData.getString("Total Stk."));
                                objMdlGodownStockData.setPendingSauda(jsonobjData.getString("Pend.Sauda"));
                                objMdlGodownStockData.setNetStock(jsonobjData.getString("Net Stk."));
                                objMdlGodownStockData.setStkBori(jsonobjData.getString("Stk.Bori"));
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

                                    objMdlGodownStockData.setArrylstAllGodowns(arrylstMdlAllGodowns);

                                    arrylstGodownStckData.add(objMdlGodownStockData);
                                }
                            }

                            mAdapter.setFilter(arrylstGodownStckData);
                        }
                    } catch (Exception e) {e.printStackTrace();}
                } else
                    showMySnackBar(getString(R.string.there_are_some_server_prblm));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                mSwiperfrshlyot.setRefreshing(false);
                mSwiperfrshlyotEmptyvw.setRefreshing(false);
                if (mFabFilters.isShown()) mFabFilters.setVisibility(View.GONE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
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

                onRefresh();
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

    // Show SnackBar with given message
    private void showMySnackBar(String message) {

        Snackbar.make(mCordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
    }
}
