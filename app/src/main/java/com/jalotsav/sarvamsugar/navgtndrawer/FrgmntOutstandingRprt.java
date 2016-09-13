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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.common.AppConstants;

import java.util.Calendar;

/**
 * Created by JALOTSAV Dev. on 20/7/16.
 */
public class FrgmntOutstandingRprt extends Fragment implements AppConstants, View.OnClickListener {

    AppCompatAutoCompleteTextView mApcmptAutoCmpltTvSlctdFiltrVal;

    DatePickerDialog mFromDatePckrDlg, mToDatePckrDlg;
    Calendar calndr;
    int crntYear, crntMonth, crntDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_outstanding_report, container, false);

        calndr = Calendar.getInstance();
        crntYear = calndr.get(Calendar.YEAR);
        crntMonth = calndr.get(Calendar.MONTH);
        crntDay = calndr.get(Calendar.DAY_OF_MONTH);

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}
