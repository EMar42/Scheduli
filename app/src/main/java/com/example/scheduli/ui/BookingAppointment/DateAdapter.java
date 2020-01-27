package com.example.scheduli.ui.BookingAppointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;
import com.example.scheduli.data.Common.Common;

import java.util.Date;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> datesList;

    private RecyclerView recyclerViewChild;
    private SlotAdapter slotAdapter;
    private List<Date> slots;
    private int numOfSlots;
    public DateAdapter(Context context, List<String> datesList, RecyclerView recyclerViewChild, SlotAdapter slotAdapter, List<Date> slots) {
        this.context = context;
        this.datesList = datesList;
        this.recyclerViewChild = recyclerViewChild;
        this.slotAdapter = slotAdapter;
        this.slots = slots;
    }





    public DateAdapter(List<String> datesList) {
        this.datesList = datesList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_appointment_view_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String date = datesList.get(position);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(holder)
    }

    @Override
    public int getItemCount() {
        return datesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.optional_date_item);
        }
    }

}
