package com.sample.carpool.carpool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.models.OfferList;
import com.sample.carpool.carpool.models.RequestedList;

import java.util.List;

public class RequestedListAdapter extends RecyclerView.Adapter<RequestedListAdapter.ViewHolder> {

    List<RequestedList> requestedLists;

    public RequestedListAdapter(List<RequestedList> rideList) {
        this.requestedLists = rideList;
    }

    @Override
    public RequestedListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the list row as a view item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.requested_ride_list, parent, false);

        return new RequestedListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestedListAdapter.ViewHolder holder, int position) {
        RequestedList requestedList = requestedLists.get(position);
        holder.tv_ol_date_time.setText(requestedList.getStart_date() + "-" + requestedList.getStart_time());
        holder.tv_ol_start_location.setText(requestedList.getStart_location());
        holder.tv_ol_end_location.setText(requestedList.getEnd_location());
    }

    @Override
    public int getItemCount() {
        return requestedLists.size();
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

