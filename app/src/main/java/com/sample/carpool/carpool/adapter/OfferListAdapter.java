package com.sample.carpool.carpool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.models.OfferList;
import com.sample.carpool.carpool.models.SelectRide;

import java.util.List;

/**
 * Created by akash on 07-03-2018.
 */

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    List<OfferList> offeredList;

    public OfferListAdapter(List<OfferList> rideList) {
        this.offeredList = rideList;
    }

    @Override
    public OfferListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the list row as a view item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_ride_list, parent, false);

        return new OfferListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OfferList offerList = offeredList.get(position);
        holder.tv_ol_date_time.setText(offerList.getStart_date() + "-" + offerList.getStart_time());
        holder.tv_ol_start_location.setText(offerList.getStart_location());
        holder.tv_ol_end_location.setText(offerList.getEnd_location());
    }

    @Override
    public int getItemCount() {
        return offeredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ol_date_time, tv_ol_start_location, tv_ol_end_location;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_ol_date_time =itemView.findViewById(R.id.ol_time_date);
            tv_ol_start_location = itemView.findViewById(R.id.ol_start_location);
            tv_ol_end_location = itemView.findViewById(R.id.ol_end_location);

        }
    }
}
