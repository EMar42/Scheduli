package com.example.scheduli.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.scheduli.data.fireBase.FirebaseQueryLiveData;
import com.example.scheduli.data.fireBase.UserDataCallBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Use this class for actions with the database that concern users.
 */
public class UserDataRepository {
    private static final String TAG_USER_REPOSITORY = "User repository";
    private static UserDataRepository instance;
    private final DatabaseReference dataBaseReference;
    private DatabaseReference userReference;
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

    public void keepInSync(String uid) {
        userReference = this.dataBaseReference.child(uid);
        userReference.keepSynced(true);
    }

    public void createNewUserInApp(String uid, User user) {
        Log.i(TAG_USER_REPOSITORY, "Created new User " + user);
        dataBaseReference.child(uid).setValue(user);
    }

    public LiveData<DataSnapshot> getUserAppointmentsSnapshot() {
        Log.i(TAG_USER_REPOSITORY, "Retrieving User appointments");
        userAppointmentsLiveData = new FirebaseQueryLiveData(userReference.child("appointments"));
        return userAppointmentsLiveData;
    }

    public void addAppointmentToUser(String uid, ArrayList<Appointment> appointments) {
        dataBaseReference.child(uid).child("appointments").setValue(appointments);
    }

    public void getUserFromUid(final String uid, final UserDataCallBack callBack) {
        ValueEventListener userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG_USER_REPOSITORY, "Getting data from database on " + uid);
                User user = dataSnapshot.child(uid).getValue(User.class);
                callBack.onCallback(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG_USER_REPOSITORY, databaseError.getMessage());
            }
        };

        this.dataBaseReference.addListenerForSingleValueEvent(userEventListener);
    }


}
