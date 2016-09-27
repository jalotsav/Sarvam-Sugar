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
import com.jalotsav.sarvamsugar.model.MdlMasterDtlsData;

import java.util.ArrayList;

/**
 * Created by JALOTSAV Dev. on 6/8/16.
 */
public class RcyclrAllMasterDtlsAdapter extends RecyclerView.Adapter<RcyclrAllMasterDtlsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<MdlMasterDtlsData> mArrylstMdlMasterDtlsData;
    int slctdExpndColpsPostn = 1001, curntExpndPostn = 1002;

    public RcyclrAllMasterDtlsAdapter(Context context, ArrayList<MdlMasterDtlsData> arrylstMdlMasterDtlsData) {

        mContext = context;
        mArrylstMdlMasterDtlsData = new ArrayList<>();
        mArrylstMdlMasterDtlsData.addAll(arrylstMdlMasterDtlsData);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lo_recyclritem_allmasterdtls, parent, false);

        ViewHolder viewHolder = new ViewHolder(mView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        MdlMasterDtlsData objMdlMasterDtlsData = mArrylstMdlMasterDtlsData.get(position);
        holder.tvPartyName.setText(mContext.getResources().getString(R.string.partyname_bracket_pcode, objMdlMasterDtlsData.getPname(), objMdlMasterDtlsData.getPcode()));
        holder.tvMobile.setText(objMdlMasterDtlsData.getMobile());
        holder.tvPhone.setText(objMdlMasterDtlsData.getPhone());
        holder.tvDalal.setText(objMdlMasterDtlsData.getDalal());
        holder.tvCntctPerson.setText(objMdlMasterDtlsData.getcPerson());
        holder.tvCRLimit.setText(objMdlMasterDtlsData.getCrLimit());
        holder.tvBori.setText(objMdlMasterDtlsData.getBori());
        holder.tvCount.setText(objMdlMasterDtlsData.getCount());
        holder.tvAvg.setText(objMdlMasterDtlsData.getAvg());
        holder.tvLastBillDays.setText(objMdlMasterDtlsData.getLastBillDays());
        holder.tvLastItem.setText(objMdlMasterDtlsData.getLastItem());
        holder.tvLastAvg.setText(objMdlMasterDtlsData.getLastAvg());

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
        return mArrylstMdlMasterDtlsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rltvlyotMain;
        public ImageView imgvwArrow;
        public TextView tvPartyName, tvMobile, tvPhone, tvCntctPerson, tvDalal, tvCRLimit,
                tvBori, tvCount, tvLastItem, tvLastAvg, tvAvg, tvLastBillDays;
        public LinearLayout lnrlyotExpandvw;

        public ViewHolder(View itemView) {
            super(itemView);

            rltvlyotMain = (RelativeLayout) itemView.findViewById(R.id.rltvlyot_recylrvw_allmasterdtls_main);
            imgvwArrow = (ImageView) itemView.findViewById(R.id.imgvw_allmasterdtls_arrow);
            tvPartyName = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_partyname);
            tvMobile = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_mobiles);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_phone);
            tvDalal = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_dalal);
            tvCntctPerson = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_cntctprsn);
            tvCRLimit = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_crlimit);
            tvBori = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_bori);
            tvCount = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_count);
            tvAvg = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_avg);
            tvLastBillDays = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_lastbillday);
            tvLastItem = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_lastitem);
            tvLastAvg = (TextView) itemView.findViewById(R.id.tv_recylrvw_allmasterdtls_lastavg);
            lnrlyotExpandvw = (LinearLayout) itemView.findViewById(R.id.lnrlyot_allmasterdtls_expandvw);
        }
    }

    public void setFilter(ArrayList<MdlMasterDtlsData> arrylstMdlMasterDtlsData) {

        mArrylstMdlMasterDtlsData = new ArrayList<>();
        mArrylstMdlMasterDtlsData.addAll(arrylstMdlMasterDtlsData);
        notifyDataSetChanged();
    }
}
