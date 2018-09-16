package com.sample.carpool.carpool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.CustomItemClickListener;
import com.sample.carpool.carpool.models.DriverList;
import com.sample.carpool.carpool.models.VehicleList;

import java.util.List;

/**
 * Created by akash on 05-04-2018.
 */

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.ViewHolder> {

    Context mContext;
    List<DriverList> driverLists;
    CustomItemClickListener customItemClickListener;

    public DriverListAdapter(Context mContext, List<DriverList> driverLists, CustomItemClickListener customItemClickListener) {
        this.mContext = mContext;
        this.driverLists = driverLists;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public DriverListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the list row as a view item
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_list, parent, false);

        final ViewHolder mViewHolder = new ViewHolder(itemView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(DriverListAdapter.ViewHolder holder, int position) {
        DriverList driverList = driverLists.get(position);
        holder.tv_licence_no.setText(driverList.getLicence_no());

        ApplyClickEvent(holder, position);
    }

    @Override
    public int getItemCount() {
        return driverLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_licence_no;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_licence_no = itemView.findViewById(R.id.licence_no_tv);
        }
    }


    private void ApplyClickEvent(final DriverListAdapter.ViewHolder holder, final int position) {
        holder.tv_licence_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customItemClickListener.onItemClick(view, position);
            }
        });
    }
}
