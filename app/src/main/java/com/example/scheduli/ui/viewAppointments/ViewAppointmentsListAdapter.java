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
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewAppointmentsListAdapter extends RecyclerView.Adapter {
    private final LayoutInflater inflater;
    private final Context context;
    private List<Appointment> appointmentList;

    public ViewAppointmentsListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.apointment_view_items, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AppointmentViewHolder appointmentViewHolder = (AppointmentViewHolder) holder;

        if (appointmentList != null) {
            //TODO clear and work on
            Appointment current = appointmentList.get(position);
            //int serviceIndex = current.getProvider().getServices().indexOf(current.getService());
            //Service service = current.getProvider().getServices().get(serviceIndex);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            //appointmentViewHolder.appointmentTitle.setText(context.getString(R.string.appointment_item_title, current.getProvider().getCompanyName(), service.getName()));
            appointmentViewHolder.appointmentDate.setText(dateFormat.format(new Date(current.getScheduledTo())));
            appointmentViewHolder.appointmentTime.setText((timeFormat.format(new Date(current.getScheduledTo()))));
            //appointmentViewHolder.appointmentPhone.setText(current.getProvider().getPhoneNumber());
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

    public void setAppointmentList(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            ArrayList<Appointment> appointments = new ArrayList<>();
            for (DataSnapshot readData : dataSnapshot.getChildren()) {
                Appointment appointment = readData.getValue(Appointment.class);
                appointments.add(appointment);
            }
            this.appointmentList = appointments;
            notifyDataSetChanged();
        }
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
