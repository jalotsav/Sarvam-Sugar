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
 */

package com.jalotsav.sarvamsugar.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.model.MdlPendngSaudaData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JALOTSAV Dev. on 18/9/16.
 */
public class RcyclrPendngSaudaAdapter extends RecyclerView.Adapter<RcyclrPendngSaudaAdapter.ViewHolder> {

    Context mContext;
    ArrayList<MdlPendngSaudaData> mArrylstMdlPendngSaudaData;
    ArrayList<Integer> mArrylstPrmryclrs;
    int slctdExpndColpsPostn = 1001, curntExpndPostn = 1002;

    public RcyclrPendngSaudaAdapter(Context context, ArrayList<MdlPendngSaudaData> arrylstMdlMasterDtlsData) {

        mContext = context;
        mArrylstMdlPendngSaudaData = new ArrayList<>();
        mArrylstMdlPendngSaudaData.addAll(arrylstMdlMasterDtlsData);
        mArrylstPrmryclrs = GeneralFuncations.getPrimaryColorArray(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lo_recyclritem_pendngsauda, parent, false);

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

        MdlPendngSaudaData objMdlPendngSaudaData = mArrylstMdlPendngSaudaData.get(position);
        holder.tvFirstChar.setText(objMdlPendngSaudaData.getPname().substring(0,1));
        holder.tvPartyName.setText(objMdlPendngSaudaData.getPname());
        holder.tvDalal.setText(objMdlPendngSaudaData.getDalal());
        holder.tvSaudaNo.setText(objMdlPendngSaudaData.getSaudaNo());
        holder.tvItem.setText(objMdlPendngSaudaData.getItem());
        holder.tvMarketRate.setText(objMdlPendngSaudaData.getMarketRate());
        holder.tvQtyInBori.setText(objMdlPendngSaudaData.getQtyInBori());
        holder.tvQty.setText(objMdlPendngSaudaData.getQty());
        holder.tvRate.setText(objMdlPendngSaudaData.getRate());
        holder.tvAmount.setText(objMdlPendngSaudaData.getAmount());

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
    }

    @Override
    public int getItemCount() {
        return mArrylstMdlPendngSaudaData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rltvlyotMain;
        public ImageView imgvwArrow;
        public TextView tvFirstChar, tvPartyName, tvDalal, tvSaudaNo, tvItem, tvMarketRate, tvQtyInBori,
                tvQty, tvRate, tvAmount;
        public LinearLayout lnrlyotExpandvw;

        public ViewHolder(View itemView) {
            super(itemView);

            rltvlyotMain = (RelativeLayout) itemView.findViewById(R.id.rltvlyot_recylrvw_pendngsauda_main);
            imgvwArrow = (ImageView) itemView.findViewById(R.id.imgvw_pendngsauda_arrow);
            tvFirstChar = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_firstchr);
            tvPartyName = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_partyname);
            tvDalal = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_dalal);
            tvSaudaNo = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_saudano);
            tvItem = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_item);
            tvMarketRate = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_mrktrate);
            tvQtyInBori = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_qtyinbori);
            tvQty = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_qty);
            tvRate = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_rate);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_recylrvw_pendngsauda_amount);
            lnrlyotExpandvw = (LinearLayout) itemView.findViewById(R.id.lnrlyot_pendngsauda_expandvw);
        }
    }

    public void setFilter(ArrayList<MdlPendngSaudaData> arrylstMdlPendngSaudaData) {

        mArrylstMdlPendngSaudaData = new ArrayList<>();
        mArrylstMdlPendngSaudaData.addAll(arrylstMdlPendngSaudaData);
        notifyDataSetChanged();
    }
}
