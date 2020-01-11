package com.example.scheduli.ui.viewAppointments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.scheduli.data.Appointment;
import com.example.scheduli.data.UserDataRepository;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class AppointmentViewModel extends ViewModel {

    private LiveData<DataSnapshot> allAppointments;
    private List<Appointment> allAppointmentsForUser;


    public AppointmentViewModel() {
        super();
        allAppointments = UserDataRepository.getInstance().getUserAppointmentsSnapshot();
    }

    public LiveData<DataSnapshot> getAllAppointments() {
        return allAppointments;
    }
}
