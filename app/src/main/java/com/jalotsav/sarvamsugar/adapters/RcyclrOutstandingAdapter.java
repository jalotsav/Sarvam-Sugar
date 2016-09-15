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
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.model.MdlOutstandingData;

import java.util.ArrayList;

/**
 * Created by JALOTSAV Dev. on 15/9/16.
 */
public class RcyclrOutstandingAdapter extends RecyclerView.Adapter<RcyclrOutstandingAdapter.ViewHolder> {

    Context mContext;
    ArrayList<MdlOutstandingData> mArrylstMdlOutstndngData;
    int slctdExpndColpsPostn = 1001, curntExpndPostn = 1002;

    public RcyclrOutstandingAdapter(Context context, ArrayList<MdlOutstandingData> arrylstMdlOutstndngData) {

        mContext = context;
        mArrylstMdlOutstndngData = new ArrayList<>();
        mArrylstMdlOutstndngData.addAll(arrylstMdlOutstndngData);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lo_recyclritem_outstndng, parent, false);

        ViewHolder viewHolder = new ViewHolder(mView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        MdlOutstandingData objMdlOutstndngData = mArrylstMdlOutstndngData.get(position);
        holder.tvPartyName.setText(objMdlOutstndngData.getPname());
        holder.tvMobile.setText(objMdlOutstndngData.getMobile());
        holder.tvPhone.setText(objMdlOutstndngData.getPhone());
        holder.tvDalal.setText(objMdlOutstndngData.getDalalName());
        holder.tvDays.setText(objMdlOutstndngData.getDays());
        holder.tvAmount.setText(objMdlOutstndngData.getAmount());
        holder.tvRecAmt.setText(objMdlOutstndngData.getRecAmt());
        holder.tvOutstanding.setText(objMdlOutstndngData.getOutstanding());
        holder.tvInvNo.setText(objMdlOutstndngData.getInvNo());
        holder.tvDate.setText(objMdlOutstndngData.getDate());
        holder.tvDueDate.setText(objMdlOutstndngData.getDueDate());

        if(slctdExpndColpsPostn != 1001) { // for first time onBind all view byDefault collapse

            if(position == slctdExpndColpsPostn) { // Expand view

                // If current selected position is EXPANDED, then COLLAPSE it.
                if(curntExpndPostn == slctdExpndColpsPostn) { // Collapse view

                    curntExpndPostn = 1002;
                    holder.imgvwArrow.animate().rotation(0).start();
                    holder.lnrlyotExpandvw.setVisibility(View.GONE);
                } else { // Expand view

                    curntExpndPostn = position;
                    holder.imgvwArrow.animate().rotation(180).start();
                    holder.lnrlyotExpandvw.setVisibility(View.VISIBLE);
                }
            } else { // Collapse view
                holder.imgvwArrow.animate().rotation(0).start();
                holder.lnrlyotExpandvw.setVisibility(View.GONE);
            }
        }

        holder.rltvlyotMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set current selected position and NotifyDataSetChange for ReBind all view
                slctdExpndColpsPostn = position;
                notifyDataSetChanged();
            }
        });

        holder.tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(holder.tvMobile.getText().toString().trim()))
                    GeneralFuncations.openDialerToCall(mContext, holder.tvMobile.getText().toString().trim());
            }
        });

        holder.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(holder.tvPhone.getText().toString().trim()))
                    GeneralFuncations.openDialerToCall(mContext, holder.tvPhone.getText().toString().trim());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrylstMdlOutstndngData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rltvlyotMain;
        public ImageView imgvwArrow;
        public TextView tvPartyName, tvMobile, tvPhone, tvDalal, tvDays,
                tvAmount, tvRecAmt, tvOutstanding, tvInvNo, tvDate, tvDueDate;
        public LinearLayout lnrlyotExpandvw;

        public ViewHolder(View itemView) {
            super(itemView);

            rltvlyotMain = (RelativeLayout) itemView.findViewById(R.id.rltvlyot_recylrvw_outstndng_main);
            imgvwArrow = (ImageView) itemView.findViewById(R.id.imgvw_outstndng_arrow);
            tvPartyName = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_partyname);
            tvMobile = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_mobiles);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_phone);
            tvDalal = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_dalal);
            tvDays = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_days);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_amount);
            tvRecAmt = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_amntrcvd);
            tvOutstanding = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_amntoutstndng);
            tvInvNo = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_invno);
            tvDate = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_date);
            tvDueDate = (TextView) itemView.findViewById(R.id.tv_recylrvw_outstndng_duedate);
            lnrlyotExpandvw = (LinearLayout) itemView.findViewById(R.id.lnrlyot_outstndng_expandvw);
        }
    }

    public void setFilter(ArrayList<MdlOutstandingData> arrylstMdlOutstndngData) {

        mArrylstMdlOutstndngData = new ArrayList<>();
        mArrylstMdlOutstndngData.addAll(arrylstMdlOutstndngData);
        notifyDataSetChanged();
    }
}
