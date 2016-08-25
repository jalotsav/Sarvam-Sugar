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
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.adapters.RcyclrGodownwiseStockAdapter;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.common.LogManager;
import com.jalotsav.sarvamsugar.common.RecyclerViewEmptySupport;
import com.jalotsav.sarvamsugar.model.MdlAllGodowns;
import com.jalotsav.sarvamsugar.model.MdlGodownStock;
import com.jalotsav.sarvamsugar.model.MdlGodownStockData;
import com.jalotsav.sarvamsugar.retrofitapihelper.APIGodownStock;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    TextView mTvAppearHere;
    RecyclerViewEmptySupport mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    FloatingActionButton mFabFilters;
    BottomSheetDialog mBottomSheetDialog;
    AppCompatButton mApcmptbtnSaveFiltr;
    TextView mTvSlctdFromDt, mTvSlctdToDt;
    LinearLayout mLnrlyotFromDt, mLnrlyotToDt;

    Calendar mCalndr;
    int mCrntYear, mCrntMonth, mCrntDay;
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

        mFabFilters.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_filter)
                .color(Color.WHITE));

        // Initialization of SwipeRefreshLayout
        initSwipeRefreshLayout(mSwiperfrshlyot);
        initSwipeRefreshLayout(mSwiperfrshlyotEmptyvw);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEmptyView(mSwiperfrshlyotEmptyvw);

        mBottomSheetDialog = new BottomSheetDialog(getActivity());

        mCalndr = Calendar.getInstance();
        mCrntYear = mCalndr.get(Calendar.YEAR);
        mCrntMonth = mCalndr.get(Calendar.MONTH);
        mCrntDay = mCalndr.get(Calendar.DAY_OF_MONTH);

        mReqstFromDT = mCrntDay + "-" + (GeneralFuncations.setMonthIn2Digit(mCrntMonth + 1)) + "-" + mCrntYear; // Format "27-07-2016"
        mReqstToDt = mCrntDay + "-" + (GeneralFuncations.setMonthIn2Digit(mCrntMonth + 1)) + "-" + mCrntYear;

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

        APIGodownStock apiGodownStck = objRetrofit.create(APIGodownStock.class);
        Call<ResponseBody> callGodownStck = apiGodownStck.getGodownStock(API_METHOD_GETGODOWNSTK, "27-07-2016", "27-07-2016");
        callGodownStck.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                mSwiperfrshlyot.setRefreshing(false);
                mSwiperfrshlyotEmptyvw.setRefreshing(false);
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {

                    ArrayList<MdlGodownStockData> arrylstGodownStckData = new ArrayList<>();
                    MdlGodownStockData objMdlGodownStockData;

                    try {

                        String websrvcRespns = response.body().string();

                        JSONObject jsnObj = new JSONObject(websrvcRespns);
                        String result = jsnObj.getString("result");
//                        String message = jsnObj.getString("message");
                        if (result.equals("0"))
                            showMySnackBar(getString(R.string.there_are_some_server_prblm));
                        else {

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

                            mAdapter = new RcyclrGodownwiseStockAdapter(getActivity(), arrylstGodownStckData);
                            mRecyclerView.setAdapter(mAdapter);

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
                if (!mFabFilters.isShown()) mFabFilters.setVisibility(View.GONE);
                showMySnackBar(getString(R.string.there_are_some_prblm));
            }
        });
    }

    private String getGodownJSON() {
        String json = "{\n" +
                "    \"method\": \"getGodownStk\",\n" +
                "    \"result\": \"1\",\n" +
                "    \"message\": \"Stock successfully retrieved...\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"ICODE\": \"B00011\",\n" +
                "            \"Item Name\": \"BARDOLI MK.\",\n" +
                "            \"Packing\": \"50KG\",\n" +
                "            \"goDowns\": \n" +
                "                {\n" +
                "                    \"F-24\": 0,\n" +
                "                    \"H-21\": 0,\n" +
                "                    \"H-22\": 24,\n" +
                "                    \"SHAHPUR\": 0\n" +
                "                }\n" +
                "            ,\n" +
                "            \"Total Stk.\": 24,\n" +
                "            \"PendSauda\": 0,\n" +
                "            \"Net Stk.\": 24,\n" +
                "            \"Stk.Bori\": 12\n" +
                "        },\n" +
                "        {\n" +
                "            \"ICODE\": \"B00035\",\n" +
                "            \"Item Name\": \"BURUKHAND KATTA\",\n" +
                "            \"Packing\": \"50KG\",\n" +
                "            \"goDowns\": \n" +
                "                {\n" +
                "                    \"F-24\": 0,\n" +
                "                    \"H-21\": 3,\n" +
                "                    \"H-22\": 0,\n" +
                "                    \"SHAHPUR\": 0\n" +
                "                }\n" +
                "            ,\n" +
                "            \"Total Stk.\": 3,\n" +
                "            \"PendSauda\": 0,\n" +
                "            \"Net Stk.\": 3,\n" +
                "            \"Stk.Bori\": 1.5\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        return json;
    }

    private void showFiltersBottomSheets() {

        View contnVw = getActivity().getLayoutInflater().inflate(R.layout.lo_bottomsheets_fromtodate, null);
        mApcmptbtnSaveFiltr = (AppCompatButton) contnVw.findViewById(R.id.apcmptbtn_btmshts_frmtodt_savefilter);
        mTvSlctdFromDt = (TextView) contnVw.findViewById(R.id.tv_btmshts_frmtodt_slctdfromdt);
        mTvSlctdToDt = (TextView) contnVw.findViewById(R.id.tv_btmshts_frmtodt_slctdtodt);
        mLnrlyotFromDt = (LinearLayout) contnVw.findViewById(R.id.lnrlyot_btmshts_frmtodt_fromdt);
        mLnrlyotToDt = (LinearLayout) contnVw.findViewById(R.id.lnrlyot_btmshts_frmtodt_todt);

        mTvSlctdFromDt.setText(mReqstFromDT);
        mTvSlctdToDt.setText(mReqstToDt);

        mBottomSheetDialog.setContentView(contnVw);
        mBottomSheetDialog.show();

        mApcmptbtnSaveFiltr.setOnClickListener(this);
        mLnrlyotFromDt.setOnClickListener(this);
        mLnrlyotToDt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_godownwise_stock_filters:

                showFiltersBottomSheets();
                break;
            case R.id.apcmptbtn_btmshts_frmtodt_savefilter:

                mBottomSheetDialog.dismiss();
//                onRefresh();
                showMySnackBar("Under Development");
                break;
            case R.id.lnrlyot_btmshts_frmtodt_fromdt:

                DatePickerDialog mFromDatePckrDlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month++;
                        mReqstFromDT = day + "-" + GeneralFuncations.setMonthIn2Digit(month) + "-" + year;
                        mTvSlctdFromDt.setText(mReqstFromDT);
//                        mTvSlctdFromDt.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(year));
                    }
                }, mCrntYear, mCrntMonth, mCrntDay);
                mFromDatePckrDlg.show();
                break;
            case R.id.lnrlyot_btmshts_frmtodt_todt:

                DatePickerDialog mToDatePckrDlg = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        month++;
                        mReqstToDt = day + "-" + GeneralFuncations.setMonthIn2Digit(month) + "-" + year;
                        mTvSlctdToDt.setText(mReqstToDt);
                    }
                }, mCrntYear, mCrntMonth, mCrntDay);
                mToDatePckrDlg.show();
                break;
        }
    }

    // Show SnackBar with given message
    private void showMySnackBar(String message) {

        Snackbar.make(mCordntrlyotMain, message, Snackbar.LENGTH_LONG).show();
    }
}
