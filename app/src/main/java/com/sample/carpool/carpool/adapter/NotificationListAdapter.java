package com.sample.carpool.carpool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.Utils.CustomItemClickListener;
import com.sample.carpool.carpool.models.NotificationList;


import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    Context mContext;
    List<NotificationList> notificationList;
    CustomItemClickListener customItemClickListener;

    public NotificationListAdapter(Context mContext, List<NotificationList> notificationList, CustomItemClickListener customItemClickListener) {
        this.mContext = mContext;
        this.notificationList = notificationList;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public NotificationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the list row as a view item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationList notifyList = notificationList.get(position);
        Log.e("ADA",notifyList.getS_name());
        holder.tv_sender_name.setText(notifyList.getS_name());
        holder.tv_sender_gender.setText(notifyList.getS_gender());
        holder.tv_sender_phone.setText(notifyList.getS_phone());
        holder.tv_pass_number.setText(notifyList.getPass_number());

        ApplyClickEvent(holder,position);



    }



    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_sender_name, tv_sender_gender, tv_sender_phone, tv_pass_number;
        Button btn_accept,btn_reject;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_sender_name = itemView.findViewById(R.id.sender_name_tv);
            tv_sender_gender = itemView.findViewById(R.id.sender_gender_tv);
            tv_sender_phone = itemView.findViewById(R.id.sender_phone_tv);
            tv_pass_number = itemView.findViewById(R.id.sender_pass_no_tv);
            btn_accept = itemView.findViewById(R.id.accept);
            btn_reject = itemView.findViewById(R.id.reject);
        }
    }
    private void ApplyClickEvent(ViewHolder holder, final int position) {
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customItemClickListener.onItemClick(view,position);

            }
        });
        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customItemClickListener.onItemClick(view,position);
            }
        });

    }

}

