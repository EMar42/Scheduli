package com.example.scheduli.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.scheduli.R;
import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

public ArrayList<Service> services;
private int selectedItem = -1;
private Context context;
private OnItemClickListener mListener;

    public ServiceAdapter(Context context, ArrayList<Service> mServices) {

        this.context = context;
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
    public void onBindViewHolder(@NonNull final ServiceViewHolder holder, final int position) {

        final Service currentItem = services.get(position);
        holder.serviceName.setText(currentItem.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int previousItem = selectedItem;
                selectedItem = position;
                mListener.onItemClick(position);
                notifyItemChanged(previousItem);
                notifyItemChanged(position);
            }
        });

        if (selectedItem == position ) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryServiceLight));
            holder.serviceName.setTextColor(context.getResources().getColor(R.color.white));

        }

        if(currentItem.getName() == null){
            holder.serviceIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_add_service_icon));
        }
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
        public CardView cardView;


        public ServiceViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            serviceIcon = itemView.findViewById(R.id.slot_frame);
            serviceName = itemView.findViewById(R.id.slot_time);
            cardView = itemView.findViewById(R.id.service_card_view);

        }

    }

    public interface OnItemClickListener{
        void  onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


}
