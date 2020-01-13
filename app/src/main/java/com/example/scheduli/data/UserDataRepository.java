package com.example.scheduli.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.scheduli.data.fireBase.FirebaseQueryLiveData;
import com.example.scheduli.utils.UsersUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Use this class for actions with the database that concern users.
 */
public class UserDataRepository {
    private static final String TAG_USER_REPOSITORY = "User repository";
    private static UserDataRepository instance;
    private final DatabaseReference dataBaseReference;
    private FirebaseQueryLiveData userAppointmentsLiveData;

    public static UserDataRepository getInstance() {
        if (instance == null) {
            synchronized (UserDataRepository.class) {
                if (instance == null) {
                    instance = new UserDataRepository();
                }
            }
        }
        return instance;
    }

    private UserDataRepository() {
        this.dataBaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public void createNewUserInApp(String uid, User user) {
        Log.i(TAG_USER_REPOSITORY, "Created new User " + user);
        dataBaseReference.child(uid).setValue(user);
    }

    public LiveData<DataSnapshot> getUserAppointmentsSnapshot() {
        Log.i(TAG_USER_REPOSITORY, "Retrieving User appointments");
        userAppointmentsLiveData = new FirebaseQueryLiveData(dataBaseReference.child(UsersUtils.getInstance().getCurrentUserUid()).child("appointments"));
        return userAppointmentsLiveData;
    }

    public void addAppointmentToUser(String uid, ArrayList<Appointment> appointments) {
        dataBaseReference.child(uid).child("appointments").setValue(appointments);
    }



}
