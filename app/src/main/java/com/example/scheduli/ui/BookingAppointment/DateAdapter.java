package com.example.scheduli.ui.BookingAppointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;

import java.util.Date;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private List<Date> datesList;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();



    public DateAdapter(List<Date> datesList) {
        this.datesList = datesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_appointment_view_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date date = datesList.get(position);
        holder.date.setText(date.toString());       // CHANGE


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
