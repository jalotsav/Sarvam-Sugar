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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.common.GeneralFuncations;
import com.jalotsav.sarvamsugar.model.MdlGodownStockData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JALOTSAV Dev. on 26/7/16.
 */
public class RcyclrGodownwiseStockAdapter extends RecyclerView.Adapter<RcyclrGodownwiseStockAdapter.ViewHolder> {

    Context mContext;
    ArrayList<MdlGodownStockData> mArrylstGodownStckData;
    ArrayList<Integer> mArrylstPrmryclrs;
    int slctdExpndColpsPostn = 1001, curntExpndPostn = 1002;

    public RcyclrGodownwiseStockAdapter(Context context, ArrayList<MdlGodownStockData> arrylstGodownStckData) {

        mContext = context;
        mArrylstGodownStckData = arrylstGodownStckData;
        mArrylstPrmryclrs = GeneralFuncations.getPrimaryColorArray(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lo_recyclritem_godownwise_stock, parent, false);

        ViewHolder viewHolder = new ViewHolder(mView);
        int n = mArrylstPrmryclrs.size();
        Random rndm_no = new Random();
        n = rndm_no.nextInt(n);

        GradientDrawable bgShape = (GradientDrawable)viewHolder.tvFirstChar.getBackground();
        bgShape.setColor(mArrylstPrmryclrs.get(n));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        MdlGodownStockData objMdlGodownStckData = mArrylstGodownStckData.get(position);
        holder.tvFirstChar.setText(objMdlGodownStckData.getItemName().substring(0,1));
        holder.tvItemName.setText(objMdlGodownStckData.getItemName());
        holder.tvPacking.setText(objMdlGodownStckData.getPacking());
        holder.tvTotalStck.setText(GeneralFuncations.get2decimalDigitOfDouble(objMdlGodownStckData.getTotalStock()));
        holder.tvPendngSauda.setText(GeneralFuncations.get2decimalDigitOfDouble(objMdlGodownStckData.getPendingSauda()));
        holder.tvNetStck.setText(GeneralFuncations.get2decimalDigitOfDouble(objMdlGodownStckData.getNetStock()));
        holder.tvStckBori.setText(GeneralFuncations.get2decimalDigitOfDouble(objMdlGodownStckData.getStkBori()));

        holder.gridvwExpandvw.setAdapter(new GridGodownwiseStockAdapter(mContext, objMdlGodownStckData.getArrylstAllGodowns()));

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
        return mArrylstGodownStckData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rltvlyotMain;
        public ImageView imgvwArrow;
        public TextView tvFirstChar, tvItemName, tvPacking, tvTotalStck, tvNetStck, tvPendngSauda, tvStckBori;
        public LinearLayout lnrlyotExpandvw;
        GridView gridvwExpandvw;

        public ViewHolder(View itemView) {
            super(itemView);

            rltvlyotMain = (RelativeLayout) itemView.findViewById(R.id.rltvlyot_recylrvw_godownstck_main);
            imgvwArrow = (ImageView) itemView.findViewById(R.id.imgvw_godownstck_arrow);
            tvFirstChar = (TextView) itemView.findViewById(R.id.tv_recylrvw_godownstck_firstchr);
            tvItemName= (TextView) itemView.findViewById(R.id.tv_recylrvw_godownstck_itemname);
            tvPacking = (TextView) itemView.findViewById(R.id.tv_recylrvw_godownstck_packing);
            tvTotalStck = (TextView) itemView.findViewById(R.id.tv_recylrvw_godownstck_totalstck);
            tvPendngSauda = (TextView) itemView.findViewById(R.id.tv_recylrvw_godownstck_pendngsauda);
            tvNetStck = (TextView) itemView.findViewById(R.id.tv_recylrvw_godownstck_netstck);
            tvStckBori = (TextView) itemView.findViewById(R.id.tv_recylrvw_godownstck_stckbori);
            lnrlyotExpandvw = (LinearLayout) itemView.findViewById(R.id.lnrlyot_godownstck_expandvw);
            gridvwExpandvw = (GridView) itemView.findViewById(R.id.gridvw_godownstck_expandvw);
        }
    }

    public void setFilter(ArrayList<MdlGodownStockData> arrylstMdlGodownStockData) {

        mArrylstGodownStckData = new ArrayList<>();
        mArrylstGodownStckData.addAll(arrylstMdlGodownStockData);
        notifyDataSetChanged();
    }
}
