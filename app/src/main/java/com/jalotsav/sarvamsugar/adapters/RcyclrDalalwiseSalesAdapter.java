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
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.model.MdlDalalwsSlsData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JALOTSAV Dev. on 27/8/16.
 */
public class RcyclrDalalwiseSalesAdapter extends RecyclerView.Adapter<RcyclrDalalwiseSalesAdapter.ViewHolder>  {


    Context mContext;
    ArrayList<MdlDalalwsSlsData> mArrylstDalalwsSlsData;
    ArrayList<Integer> mArrylstPrmryclrs;

    public RcyclrDalalwiseSalesAdapter(Context context, ArrayList<MdlDalalwsSlsData> arrylstDalalwsSlsData) {

        mContext = context;
        mArrylstDalalwsSlsData = arrylstDalalwsSlsData;
        mArrylstPrmryclrs = GeneralFuncations.getPrimaryColorArray(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lo_recyclritem_dalalwisesales, parent, false);

        ViewHolder viewHolder = new ViewHolder(mView);
        int n = mArrylstPrmryclrs.size();
        Random rndm_no = new Random();
        n = rndm_no.nextInt(n);

        GradientDrawable bgShape = (GradientDrawable)viewHolder.tvFirstChar.getBackground();
        bgShape.setColor(mArrylstPrmryclrs.get(n));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        MdlDalalwsSlsData objMdlData = mArrylstDalalwsSlsData.get(position);
        holder.tvFirstChar.setText(objMdlData.getDalal().substring(0,1));
        holder.tvDalalName.setText(mContext.getResources().getString(R.string.partyname_bracket_pcode, objMdlData.getDalal(), objMdlData.getDpCode()));
        holder.tvMadhur.setText(objMdlData.getMadhur());
        holder.tvOther.setText(objMdlData.getOther());
        holder.tvMobile.setText(objMdlData.getMobile());
        holder.tvTotalBori.setText(objMdlData.getTotalBori());

        holder.tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(holder.tvMobile.getText().toString().trim()))
                    GeneralFuncations.openDialerToCall(mContext, holder.tvMobile.getText().toString().trim());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrylstDalalwsSlsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvFirstChar, tvDalalName, tvMadhur, tvOther, tvMobile, tvTotalBori;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFirstChar = (TextView) itemView.findViewById(R.id.tv_recylrvw_dalalwssls_firstchr);
            tvDalalName = (TextView) itemView.findViewById(R.id.tv_recylrvw_dalalwssls_dalalname);
            tvMadhur = (TextView) itemView.findViewById(R.id.tv_recylrvw_dalalwssls_madhur);
            tvOther = (TextView) itemView.findViewById(R.id.tv_recylrvw_dalalwssls_other);
            tvMobile = (TextView) itemView.findViewById(R.id.tv_recylrvw_dalalwssls_mobile);
            tvTotalBori = (TextView) itemView.findViewById(R.id.tv_recylrvw_dalalwssls_totalbori);
        }
    }

    public void setFilter(ArrayList<MdlDalalwsSlsData> arrylstMdlDalalwsSlsData) {

        mArrylstDalalwsSlsData = new ArrayList<>();
        mArrylstDalalwsSlsData.addAll(arrylstMdlDalalwsSlsData);
        notifyDataSetChanged();
    }
}
