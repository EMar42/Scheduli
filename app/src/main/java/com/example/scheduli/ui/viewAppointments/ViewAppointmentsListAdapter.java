package com.example.scheduli.ui.viewAppointments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;
import com.example.scheduli.data.joined.JoinedAppointment;
import com.example.scheduli.ui.appointmentDetails.AppointmentDetailsActivity;
import com.example.scheduli.utils.TriggerCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ViewAppointmentsListAdapter extends RecyclerView.Adapter implements Filterable {
    private final LayoutInflater inflater;
    private final Context context;

    private List<JoinedAppointment> joinedAppointments;
    private List<JoinedAppointment> shownJoinedAppointments;
    private final TriggerCallback callback;

    public List<JoinedAppointment> getJoinedAppointments() {
        return joinedAppointments;
    }

    ViewAppointmentsListAdapter(Context context, TriggerCallback triggerCallback) {
        callback = triggerCallback;
        this.context = context;
        inflater = LayoutInflater.from(context);
        joinedAppointments = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.apointment_view_items, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final AppointmentViewHolder appointmentViewHolder = (AppointmentViewHolder) holder;

        if (shownJoinedAppointments != null && shownJoinedAppointments.size() > 0) {
            final JoinedAppointment current = shownJoinedAppointments.get(position);

            //set title for appointment
            String titleString = context.getString(R.string.appointment_item_title, current.getProviderCompanyName(), current.getAppointment().getServiceName());

            appointmentViewHolder.appointmentTitle.setText(titleString);
            appointmentViewHolder.appointmentAddress.setText(current.getProviderAddress());
            appointmentViewHolder.appointmentPhone.setText(current.getProviderPhoneNumber());

            //set date of appointment
            Date date = new Date(current.getAppointment().getStart());
            appointmentViewHolder.appointmentDate.setText(DateFormat.getDateInstance().format(date));

            //set time of appointment
            Date start = new Date(current.getAppointment().getStart());
            Date end = new Date(current.getAppointment().getEnd());
            String timeString = DateFormat.getTimeInstance().format(start) + " - " + DateFormat.getTimeInstance().format(end);
            appointmentViewHolder.appointmentTime.setText(timeString);

            if (current.getAppointment().getAlarmReminderTime() != 0 && new Date(Calendar.getInstance().getTimeInMillis()).after(new Date(current.getAppointment().getAlarmReminderTime()))) {
                @SuppressLint("SimpleDateFormat") DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                appointmentViewHolder.appointmentAlarm.setText("Alarm set for " + simpleDateFormat.format(new Date(current.getAppointment().getAlarmReminderTime())));
            }
            appointmentViewHolder.currentPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        if (shownJoinedAppointments != null) {
            return shownJoinedAppointments.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return timeFilter;
    }


    //setup filter for appointments
    private Filter timeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<JoinedAppointment> filteredList = new ArrayList<>();

            if (constraint.equals("future")) {
                Date currentTime = android.icu.util.Calendar.getInstance().getTime();
                for (JoinedAppointment appointment : joinedAppointments) {
                    Date appointmentStartTime = new Date(appointment.getAppointment().getStart());
                    if (currentTime.before(appointmentStartTime))
                        filteredList.add(appointment);
                }
            } else if (constraint.equals("past")) {
                Date currentTime = android.icu.util.Calendar.getInstance().getTime();
                for (JoinedAppointment appointment : joinedAppointments) {
                    Date appointmentStartTime = new Date(appointment.getAppointment().getStart());
                    if (currentTime.after(appointmentStartTime))
                        filteredList.add(appointment);
                }
            } else {
                filteredList.addAll(joinedAppointments);
            }

            //end of filtering
            Collections.sort(filteredList, JoinedAppointment.BY_DATETIME_DESCENDING);
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (shownJoinedAppointments != null) {
                shownJoinedAppointments.clear();
                shownJoinedAppointments.addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };



    void triggerSorting() {
        if (shownJoinedAppointments != null) {
            Collections.sort(shownJoinedAppointments, JoinedAppointment.BY_DATETIME_DESCENDING);
        }
    }

    void setJoinedAppointments(ArrayList<JoinedAppointment> joinedAppointments) {
        this.joinedAppointments = joinedAppointments;
        this.shownJoinedAppointments = new ArrayList<>(joinedAppointments);
        notifyDataSetChanged();
        callback.onCallback();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView appointmentTitle;
        TextView appointmentDate;
        TextView appointmentTime;
        TextView appointmentPhone;
        TextView appointmentAddress;
        TextView appointmentAlarm;
        int currentPosition;

        AppointmentViewHolder(@NonNull final View itemView) {
            super(itemView);

            appointmentTitle = itemView.findViewById(R.id.tv_item_appointmentTitle);
            appointmentDate = itemView.findViewById(R.id.tv_item_appointmentDate);
            appointmentTime = itemView.findViewById(R.id.tv_item_appointmentTime);
            appointmentPhone = itemView.findViewById(R.id.tv_item_appointmentContact);
            appointmentAddress = itemView.findViewById(R.id.tv_item_appointment_address);
            appointmentAlarm = itemView.findViewById(R.id.tv_item_appointment_alarm);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shownJoinedAppointments.size() > 0) {
                        Intent detailsIntent = new Intent(context, AppointmentDetailsActivity.class);
                        detailsIntent.putExtra(AppointmentDetailsActivity.APPOINTMENT_DETAILS, shownJoinedAppointments.get(currentPosition));
                        context.startActivity(detailsIntent);
                    }
                }
            });
        }

    }
}
