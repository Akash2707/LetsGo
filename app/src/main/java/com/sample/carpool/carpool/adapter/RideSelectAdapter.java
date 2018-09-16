package com.sample.carpool.carpool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.CustomItemClickListener;
import com.sample.carpool.carpool.models.SelectRide;

import java.util.List;

/**
 * Created by akash on 03-03-2018.
 */

public class RideSelectAdapter extends RecyclerView.Adapter<RideSelectAdapter.ViewHolder> {

    Context mContext;
    List<SelectRide> rideList;
    CustomItemClickListener customItemClickListener;

   /* public RideSelectAdapter(List<SelectRide> rideList) {
        this.rideList = rideList;
    }*/

    public RideSelectAdapter(Context mContext, List<SelectRide> rideList, CustomItemClickListener customItemClickListener) {
        this.mContext = mContext;
        this.rideList = rideList;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public RideSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the list row as a view item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SelectRide rideDetails = rideList.get(position);
        holder.tv_date_time.setText(rideDetails.getStart_date() + "-" + rideDetails.getStart_time());
        holder.tv_amount.setText("₹" + rideDetails.getTotal_price().toString());
        holder.tv_start_location.setText(rideDetails.getStart_location());
        holder.tv_end_location.setText(rideDetails.getEnd_location());
        holder.tv_car_name.setText(rideDetails.getCar_name());
        holder.tv_seats.setText(rideDetails.getAvailable_seats());

        ApplyClickEvent(holder, position);

    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date_time, tv_amount, tv_start_location, tv_end_location, tv_car_name, tv_seats;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date_time = itemView.findViewById(R.id.time_date);
            tv_amount = itemView.findViewById(R.id.amount);
            tv_start_location = itemView.findViewById(R.id.start_location);
            tv_end_location = itemView.findViewById(R.id.end_location);
            tv_car_name = itemView.findViewById(R.id.car_name);
            tv_seats = itemView.findViewById(R.id.seats);
            btn = itemView.findViewById(R.id.ride_select_btn);

        }
    }

    private void ApplyClickEvent(final ViewHolder holder, final int position) {
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customItemClickListener.onItemClick(view, position);
            }
        });
    }
}