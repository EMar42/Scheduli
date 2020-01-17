package com.example.scheduli.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;


import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

public ArrayList<Service> services;
private OnItemClickListener mListener;

    public ServiceAdapter(ArrayList<Service> mServices) {

        this.services = mServices;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_view_item, parent, false);
        ServiceViewHolder serviceViewHolder = new ServiceViewHolder(view, mListener);

        return serviceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {

        Service currentItem = services.get(position);

        holder.serviceName.setText(currentItem.getName());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    //get item position
    public interface OnServiceListener {
        void onServiceClick(int position);
    }


    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        public ImageView serviceIcon;
        public TextView serviceName;


        public ServiceViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            serviceIcon = itemView.findViewById(R.id.slot_frame);
            serviceName = itemView.findViewById(R.id.slot_time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });

        }


    }

    public interface OnItemClickListener{
        void  onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){

        mListener = listener;
    }


}
