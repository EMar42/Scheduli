package com.example.scheduli.ui.appointmentDetails;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.example.scheduli.data.joined.JoinedAppointment;
import com.example.scheduli.utils.TriggerCallback;

import java.util.Calendar;

public class AppointmentDetailsViewModel extends ViewModel {
    private JoinedAppointment joinedAppointment;
    private Bitmap providerImage;

    private Calendar alarmSetTimeFromDialog;
    private Calendar alarmSetDateFromDialog;

    private TriggerCallback dateCallback;
    private TriggerCallback timeCallback;

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

    public void setAlarmDate(Calendar alarmSetDateFromDialog) {
        this.alarmSetDateFromDialog = alarmSetDateFromDialog;
        dateCallback.onCallback();
    }

    public void setAlarmTime(Calendar alarmSetTimeFromDialog) {
        this.alarmSetTimeFromDialog = alarmSetTimeFromDialog;
        timeCallback.onCallback();
    }

    public Calendar getAlarmSetTimeFromDialog() {
        return alarmSetTimeFromDialog;
    }

    public Calendar getAlarmSetDateFromDialog() {
        return alarmSetDateFromDialog;
    }

    public void setDateCallback(TriggerCallback dateCallback) {
        this.dateCallback = dateCallback;
    }

    public void setTimeCallback(TriggerCallback timeCallback) {
        this.timeCallback = timeCallback;
    }
}
