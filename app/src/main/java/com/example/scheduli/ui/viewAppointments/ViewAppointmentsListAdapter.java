package com.example.scheduli.ui.viewAppointments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;
import com.example.scheduli.data.Appointment;

import java.util.List;

public class ViewAppointmentsListAdapter extends RecyclerView.Adapter {
    //TODO implement class
    private final LayoutInflater inflater;
    private List<Appointment> appointmentList;

    public ViewAppointmentsListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null; //TODO decide where to inflate the view.
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AppointmentViewHolder appointmentViewHolder = (AppointmentViewHolder) holder;

        if (appointmentList != null) {
            //TODO implement string manipulation for appointment text views

        } else {
            appointmentViewHolder.appointmentTitle.setText(R.string.no_appointment_found);
            appointmentViewHolder.appointmentPhone.setText("");
            appointmentViewHolder.appointmentDate.setText("");
            appointmentViewHolder.appointmentTime.setText("");
            appointmentViewHolder.rescheduleButton.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        if (appointmentList != null) {
            return appointmentList.size();
        }

        return 0;
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentTitle;
        TextView appointmentDate;
        TextView appointmentTime;
        TextView appointmentPhone;
        Button rescheduleButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            appointmentTitle = itemView.findViewById(R.id.tv_item_appointmentTitle);
            appointmentDate = itemView.findViewById(R.id.tv_item_appointmentDate);
            appointmentTime = itemView.findViewById(R.id.tv_item_appointmentTime);
            appointmentPhone = itemView.findViewById(R.id.tv_item_appointmentContact);
            rescheduleButton = itemView.findViewById(R.id.btn_item_rescheduleAppoitment);
        }
    }
}
