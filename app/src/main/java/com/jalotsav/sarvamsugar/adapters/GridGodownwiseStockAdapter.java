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

package com.jalotsav.sarvamsugar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.model.MdlAllGodowns;

import java.util.ArrayList;

/**
 * Created by JALOTSAV Dev. on 24/8/16.
 */
public class GridGodownwiseStockAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<MdlAllGodowns> mArrylstAllGodowns = new ArrayList<>();

    public GridGodownwiseStockAdapter(Context mContext, ArrayList<MdlAllGodowns> arralstAllGodowns) {
        this.mContext = mContext;
        mArrylstAllGodowns.addAll(arralstAllGodowns);
    }

    @Override
    public int getCount() {
        return mArrylstAllGodowns.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View holder = view;
        if (holder == null) {

            holder = LayoutInflater.from(mContext).inflate(R.layout.lo_griditem_godownwise_stock, viewGroup, false);
        }

        TextView tvGodown1Name = (TextView) holder.findViewById(R.id.tv_gridvw_godownstck_gdname);
        TextView tvGodown1Stock = (TextView) holder.findViewById(R.id.tv_gridvw_godownstck_gdstock);

        MdlAllGodowns objMdlAllGodowns = mArrylstAllGodowns.get(i);
        tvGodown1Name.setText(objMdlAllGodowns.getGodownName());
        tvGodown1Stock.setText(objMdlAllGodowns.getGodownStock());

        return holder;
    }
}
