package com.sample.carpool.carpool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.CustomItemClickListener;
import com.sample.carpool.carpool.models.OfferList;
import com.sample.carpool.carpool.models.VehicleList;

import java.util.List;

/**
 * Created by akash on 08-03-2018.
 */

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder>{

    Context mContext;
    List<VehicleList> vehicleLists;
    CustomItemClickListener customItemClickListener;

//    public VehicleListAdapter(Context mContext, List<VehicleList> vehicleLists) {
//        this.mContext = mContext;
//        this.vehicleLists = vehicleLists;
//    }


    public VehicleListAdapter(Context mContext, List<VehicleList> vehicleLists, CustomItemClickListener customItemClickListener) {
        this.mContext = mContext;
        this.vehicleLists = vehicleLists;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public VehicleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the list row as a view item
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_list, parent, false);

        final ViewHolder mViewHolder = new ViewHolder(itemView);
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                customItemClickListener.onItemClick(view,mViewHolder.getAdapterPosition());
//            }
//        });
        return mViewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       VehicleList vehicleList = vehicleLists.get(position);
       holder.tv_car_no.setText(vehicleList.getRegistration_no());

        ApplyClickEvent(holder,position);

    }

    @Override
    public int getItemCount() {
        return vehicleLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_car_no;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_car_no = itemView.findViewById(R.id.car_no_tv);


        }
    }

    private void ApplyClickEvent(final ViewHolder holder,final int position){
        holder.tv_car_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customItemClickListener.onItemClick(view,position);
            }
        });
    }



}
