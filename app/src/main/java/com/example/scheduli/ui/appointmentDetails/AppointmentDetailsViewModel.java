package com.example.scheduli.ui.appointmentDetails;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.example.scheduli.data.joined.JoinedAppointment;

public class AppointmentDetailsViewModel extends ViewModel {
    private JoinedAppointment joinedAppointment;
    private Bitmap providerImage;

    public AppointmentDetailsViewModel() {
        this.joinedAppointment = null;
        this.providerImage = null;
    }

    void setJoinedAppointment(JoinedAppointment joinedAppointment) {
        this.joinedAppointment = joinedAppointment;
    }

    public Bitmap getProviderImage() {
        return providerImage;
    }

    void setProviderImage(Bitmap providerImage) {
        this.providerImage = providerImage;
    }

    public JoinedAppointment getJoinedAppointment() {
        return joinedAppointment;
    }
}
