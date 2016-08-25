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
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.LogManager;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by JALOTSAV Dev. on 20/7/16.
 */
public class FrgmntOutstandingRprt extends Fragment implements AppConstants, View.OnClickListener {

    FloatingActionButton mFabFilters;
    BottomSheetDialog mBottomSheetDialog;
    AppCompatButton mApcmptbtnSaveFiltr;
    TextView mTvSlctdFromDt, mTvSlctdToDt;
    Spinner mSpnrFilterBy, mSpnrSlctdFilteVal;
    LinearLayout mLnrlyotFromDt, mLnrlyotToDt, lnrlyotFromToDt, lnrlyotSlctdFilteVal;
    AppCompatAutoCompleteTextView mApcmptAutoCmpltTvSlctdFiltrVal;

    DatePickerDialog mFromDatePckrDlg, mToDatePckrDlg;
    Calendar calndr;
    int crntYear, crntMonth, crntDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_outstanding_report, container, false);

        mFabFilters = (FloatingActionButton) rootView.findViewById(R.id.fab_outstndngrprt_filters);

        mBottomSheetDialog = new BottomSheetDialog(getActivity());

        calndr = Calendar.getInstance();
        crntYear = calndr.get(Calendar.YEAR);
        crntMonth = calndr.get(Calendar.MONTH);
        crntDay = calndr.get(Calendar.DAY_OF_MONTH);

        mFabFilters.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(CommunityMaterial.Icon.cmd_filter)
                .color(Color.WHITE));

        mFabFilters.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_outstndngrprt_filters:
                showFiltersBottomSheets();
                break;
            case R.id.apcmptbtn_btmshts_cntnt_savefilter:
                mBottomSheetDialog.dismiss();
                Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lnrlyot_btmshts_cntnt_fromdt:

//                showDialog(FROM_DATE_DTPICKER);
                break;
            case R.id.lnrlyot_btmshts_cntnt_todt:

//                showDialog(TO_DATE_DTPICKER);
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == FROM_DATE_DTPICKER) {

            mFromDatePckrDlg = new DatePickerDialog(getActivity(), sanghStartDateListener, crntYear, crntMonth, crntDay);
            mFromDatePckrDlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            return mFromDatePckrDlg;
        }else if (id == TO_DATE_DTPICKER) {

            mToDatePckrDlg = new DatePickerDialog(getActivity(), sanghEndDateListener, crntYear, crntMonth, crntDay);
            mToDatePckrDlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            return mToDatePckrDlg;
        }
        return null;
    }

    private void showFiltersBottomSheets() {

        View contnVw = getActivity().getLayoutInflater().inflate(R.layout.lo_bottomsheets_content, null);
        mApcmptbtnSaveFiltr = (AppCompatButton) contnVw.findViewById(R.id.apcmptbtn_btmshts_cntnt_savefilter);
        mTvSlctdFromDt = (TextView) contnVw.findViewById(R.id.tv_btmshts_cntnt_slctdfromdt);
        mTvSlctdToDt = (TextView) contnVw.findViewById(R.id.tv_btmshts_cntnt_slctdtodt);
        mSpnrFilterBy = (Spinner) contnVw.findViewById(R.id.spnr_btmshts_cntnt_filterby);
        mSpnrSlctdFilteVal = (Spinner) contnVw.findViewById(R.id.spnr_btmshts_cntnt_slctdfilterval);
        lnrlyotFromToDt = (LinearLayout) contnVw.findViewById(R.id.lnrlyot_btmshts_cntnt_fromtodate);
        lnrlyotSlctdFilteVal = (LinearLayout) contnVw.findViewById(R.id.lnrlyot_btmshts_cntnt_slctdfilterval);
        mLnrlyotFromDt = (LinearLayout) contnVw.findViewById(R.id.lnrlyot_btmshts_cntnt_fromdt);
        mLnrlyotToDt = (LinearLayout) contnVw.findViewById(R.id.lnrlyot_btmshts_cntnt_todt);
        mApcmptAutoCmpltTvSlctdFiltrVal = (AppCompatAutoCompleteTextView) contnVw.findViewById(R.id.apcmptautocmplttv_btmshts_cntnt_slctdfilterval);

        mBottomSheetDialog.setContentView(contnVw);
        mBottomSheetDialog.show();

        /*CoordinatorLayout cordntlyot = (CoordinatorLayout) contnVw.findViewById(R.id.lnrlyot_bottomsheets_filterby);
        View vw = cordntlyot.findViewById(R.id.lnrlyot_main);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(vw);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(200);*/

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getActivity(),android.R.layout.select_dialog_item, getResources().getStringArray(R.array.arry_temp));
        mApcmptAutoCmpltTvSlctdFiltrVal.setThreshold(2);
        mApcmptAutoCmpltTvSlctdFiltrVal.setAdapter(adapter);

        mApcmptbtnSaveFiltr.setOnClickListener(this);
        mLnrlyotFromDt.setOnClickListener(this);
        mLnrlyotToDt.setOnClickListener(this);

        mSpnrFilterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0 || position == 1) {
                    lnrlyotFromToDt.setVisibility(View.VISIBLE);
                    lnrlyotSlctdFilteVal.setVisibility(View.GONE);
                } else {
                    lnrlyotFromToDt.setVisibility(View.GONE);
                    lnrlyotSlctdFilteVal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private DatePickerDialog.OnDateSetListener sanghStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // TODO Auto-generated method stub

            month++; // Month is count 0 to 11

            if(crntDay > day && crntMonth >= month && crntYear >= year){

                Toast.makeText(getActivity(), "Select valid From date", Toast.LENGTH_SHORT).show();
                /*sanghStartDateSetStatus = false;
                tvSanghStartdateLbl.setVisibility(View.INVISIBLE);
                tvSanghStartdateError.setText(getResources().getString(R.string.slct_vald_sangh_start_date));
                tvSanghStartdateError.setVisibility(View.VISIBLE);*/

//                mApcmptbtnFromdt.setText(getResources().getString(R.string.from_date_sml));
            } else{

                /*sanghStartDateSetStatus = true;
                tvSanghStartdateError.setVisibility(View.GONE);
                tvSanghStartdateLbl.setVisibility(View.VISIBLE);*/

//                mApcmptbtnFromdt.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
                mTvSlctdFromDt.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));

                // convert date to TimeStamp and store into static variable
//                SANGH_START_DATE = General_Fnctns.getTimestamp_fromdate(btnSanghStartdate.getText().toString().trim());
            }
            /*if(sanghEndDateSetStatus){

                if(mToDatePckrDlg.getDatePicker().getDayOfMonth() < day
                        && mToDatePckrDlg.getDatePicker().getMonth() <= month
                        && mToDatePckrDlg.getDatePicker().getYear() <= year){

                    sanghEndDateSetStatus = false;
                    tvSanghEnddateLbl.setVisibility(View.INVISIBLE);
                    tvSanghEnddateError.setText(getResources().getString(R.string.slct_vald_sangh_end_date));
                    tvSanghEnddateError.setVisibility(View.VISIBLE);
                    btnSanghEnddate.setText(getResources().getString(R.string.sangh_end_date));
                }
            }*/
        }
    };

    private DatePickerDialog.OnDateSetListener sanghEndDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // TODO Auto-generated method stub

            month++; // Month is count 0 to 11

            /*if(!sanghStartDateSetStatus){

                sanghEndDateSetStatus = false;
                tvSanghEnddateLbl.setVisibility(View.INVISIBLE);
                tvSanghEnddateError.setText(getResources().getString(R.string.slct_sangh_start_date));
                tvSanghEnddateError.setVisibility(View.VISIBLE);
                btnSanghEnddate.setText(getResources().getString(R.string.sangh_end_date));
            } else*/ if(crntDay > day && crntMonth >= month && crntYear >= year){

                Toast.makeText(getActivity(), "Select valid To date", Toast.LENGTH_SHORT).show();
                /*sanghEndDateSetStatus = false;
                tvSanghEnddateLbl.setVisibility(View.INVISIBLE);
                tvSanghEnddateError.setText(getResources().getString(R.string.slct_vald_sangh_end_date));
                tvSanghEnddateError.setVisibility(View.VISIBLE);*/

//                mApcmptbtnTodt.setText(getResources().getString(R.string.to_date_sml));
            } else if(mFromDatePckrDlg.getDatePicker().getDayOfMonth() > day
                    && mFromDatePckrDlg.getDatePicker().getMonth() >= month
                    && mFromDatePckrDlg.getDatePicker().getYear() >= year){

                Toast.makeText(getActivity(), "Select valid To date", Toast.LENGTH_SHORT).show();

                /*sanghEndDateSetStatus = false;
                tvSanghEnddateLbl.setVisibility(View.INVISIBLE);
                tvSanghEnddateError.setText(getResources().getString(R.string.slct_vald_sangh_end_date));
                tvSanghEnddateError.setVisibility(View.VISIBLE);*/

//                mApcmptbtnTodt.setText(getResources().getString(R.string.to_date_sml));
            } else{

                /*sanghEndDateSetStatus = true;
                tvSanghEnddateError.setVisibility(View.GONE);
                tvSanghEnddateLbl.setVisibility(View.VISIBLE);*/

//                mApcmptbtnTodt.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
                mTvSlctdToDt.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));

                // convert date to TimeStamp and store into static variable
//                SANGH_END_DATE = General_Fnctns.getTimestamp_fromdate(btnSanghEnddate.getText().toString().trim());
            }
        }
    };
}
