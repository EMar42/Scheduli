package com.example.scheduli.ui.appointmentDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduli.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class AppointemtDetailsFragment extends Fragment {

    private AppointmentDetailsViewModel mViewModel;

    //Fragment controls
    private TextView appointmentServiceName;
    private TextView appointmentDate;
    private TextView appointmentTime;
    private TextView appointmentServiceCost;
    private TextView alertTimeSet;
    private TextView alertDateSet;
    private Button setAlertTimeButton;
    private Button setAlertDateButton;
    private Button callAlertButton;
    private Button setAppointmentInCalenderButton;
    private Button cancelAppointmentButton;


    public static AppointemtDetailsFragment newInstance() {
        return new AppointemtDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.appointemt_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(AppointmentDetailsViewModel.class);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        appointmentServiceCost.setText(currencyFormat.format(Float.valueOf(mViewModel.getJoinedAppointment().getAppointment().getServiceCost())));

        Calendar appointmentStart = Calendar.getInstance();
        Calendar appointmentEnd = Calendar.getInstance();
        appointmentStart.setTimeInMillis(mViewModel.getJoinedAppointment().getAppointment().getStart());
        appointmentEnd.setTimeInMillis(mViewModel.getJoinedAppointment().getAppointment().getEnd());

        appointmentDate.setText(DateFormat.getDateInstance().format(appointmentStart.getTime()));
        appointmentServiceName.setText(mViewModel.getJoinedAppointment().getAppointment().getServiceName());

        //TODO add time range for the appointment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

    }

    private void initView(@NonNull View view) {
        appointmentServiceName = view.findViewById(R.id.tv_app_details_service_name);
        appointmentDate = view.findViewById(R.id.tv_app_details_appointment_date);
        appointmentTime = view.findViewById(R.id.tv_item_appointmentTime);
        appointmentServiceCost = view.findViewById(R.id.tv_app_details_service_cost);
        alertDateSet = view.findViewById(R.id.tv_app_details_alarm_selected_date);
        alertTimeSet = view.findViewById(R.id.tv_app_details_alarm_selected_time);
        setAlertDateButton = view.findViewById(R.id.btn_app_details_alarm_set_date);
        setAlertTimeButton = view.findViewById(R.id.btn_app_details_alarm_set_time);
        setAppointmentInCalenderButton = view.findViewById(R.id.btn_app_details_set_appointment_in_calender);
        callAlertButton = view.findViewById(R.id.btn_app_details_alarm_activate);
        cancelAppointmentButton = view.findViewById(R.id.btn_app_details_cancel_appointment);
    }
}
