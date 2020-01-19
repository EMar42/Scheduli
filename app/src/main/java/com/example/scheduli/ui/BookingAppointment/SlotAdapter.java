package com.example.scheduli.ui.BookingAppointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.ViewHolder> {


    private List<Date> slotsList;

    public SlotAdapter(List<Date> slotsList) {
        this.slotsList = slotsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Date currentItem = slotsList.get(position);
        long hour = (currentItem.getTime()  / (1000 * 60 * 60)) % 24;
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        holder.slotTime.setText(dateFormat.format(currentItem));
//        holder.slotPmAm.setText(DateFormat.getInstance().format(currentItem).);
    }

    @Override
    public int getItemCount() {
        return slotsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView slotTime;
//        TextView slotPmAm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            slotPmAm = (TextView) itemView.findViewById(R.id.slot_pm_am);
            slotTime = (TextView) itemView.findViewById(R.id.slot_time);
        }
    }
}
