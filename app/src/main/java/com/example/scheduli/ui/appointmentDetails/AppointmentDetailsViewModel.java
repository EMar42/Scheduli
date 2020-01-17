package com.example.scheduli.ui.appointmentDetails;

import androidx.lifecycle.ViewModel;

import com.example.scheduli.data.joined.JoinedAppointment;

public class AppointmentDetailsViewModel extends ViewModel {
    private JoinedAppointment joinedAppointment;

    public AppointmentDetailsViewModel() {
        this.joinedAppointment = null;
    }

    public void setJoinedAppointment(JoinedAppointment joinedAppointment) {
        this.joinedAppointment = joinedAppointment;
    }

    public JoinedAppointment getJoinedAppointment() {
        return joinedAppointment;
    }
}
