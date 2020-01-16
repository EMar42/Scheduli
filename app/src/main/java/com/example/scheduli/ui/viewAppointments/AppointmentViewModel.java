package com.example.scheduli.ui.viewAppointments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.scheduli.data.Appointment;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.example.scheduli.data.joined.JoinedAppointment;
import com.example.scheduli.data.repositories.ProviderDataRepository;
import com.example.scheduli.data.repositories.UserDataRepository;

import java.util.ArrayList;

public class AppointmentViewModel extends ViewModel {

    private MutableLiveData<ArrayList<JoinedAppointment>> allJoinedAppointments;
    private ArrayList<JoinedAppointment> savedAppointments;


    public AppointmentViewModel() {
        super();

        allJoinedAppointments = new MutableLiveData<>();

        //Call database for appointments of user
        pullAppointemmntsOfUser(new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                //receive joined appointment from database
                if (allJoinedAppointments.getValue() == null) {
                    allJoinedAppointments.setValue(new ArrayList<JoinedAppointment>());
                }
                JoinedAppointment appointment = (JoinedAppointment) object;
                ArrayList<JoinedAppointment> joinedAppointments = allJoinedAppointments.getValue();
                joinedAppointments.add(appointment);
                allJoinedAppointments.setValue(joinedAppointments);
            }
        });
    }

    public MutableLiveData<ArrayList<JoinedAppointment>> getAllJoinedAppointments() {
        return allJoinedAppointments;
    }

    private void pullAppointemmntsOfUser(final DataBaseCallBackOperation callBackOperation) {
        UserDataRepository.getInstance().getUserAppointments(new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                ArrayList<Appointment> appointments = (ArrayList<Appointment>) object;

                for (final Appointment appointment : appointments) {

                    ProviderDataRepository.getInstance().getProviderByUid(appointment.getProviderUid(), new DataBaseCallBackOperation() {
                        @Override
                        public void callBack(Object object) {
                            Provider provider = (Provider) object;
                            JoinedAppointment joinedAppointment = new JoinedAppointment(appointment, provider.getImageUrl(), provider.getCompanyName(), provider.getProfession(), provider.getPhoneNumber(), provider.getAddress());
                            callBackOperation.callBack(joinedAppointment);
                        }
                    });
                }
            }
        });
    }

    public ArrayList<JoinedAppointment> getSavedAppointments() {
        return savedAppointments;
    }

    public void setSavedAppointments(ArrayList<JoinedAppointment> savedAppointments) {
        this.savedAppointments = savedAppointments;
    }

}
