package com.example.scheduli.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.scheduli.data.Appointment;
import com.example.scheduli.data.User;
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.example.scheduli.data.fireBase.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Use this class for actions with the database that concern users.
 */
public class UserDataRepository {
    private static final String TAG_USER_REPOSITORY = "User repository";
    private static UserDataRepository instance;
    private final DatabaseReference dataBaseReference;
    private DatabaseReference userReference;
    private ValueEventListener appointmentListener;

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
        return new FirebaseQueryLiveData(userReference.child("appointments"));
    }


    public void addAppointmentsListToUser(String uid, ArrayList<Appointment> appointments) {
        dataBaseReference.child(uid).child("appointments").setValue(appointments);
    }

    public void addSingleAppointmentToUser(final String uid, final Appointment appointment) {
        final DataBaseCallBackOperation callBackOperation = new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                Integer index = (Integer) object;

                dataBaseReference.child(uid).child("appointments").child(Integer.toString(index)).setValue(appointment);
            }
        };

        dataBaseReference.child(uid).child("appointments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int max = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    int key = Integer.parseInt(Objects.requireNonNull(child.getKey()));
                    if (key > max)
                        max = key;
                }

                callBackOperation.callBack(max + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Call this to get a user from the database
     */
    public void getUserFromUid(final String uid, final DataBaseCallBackOperation callBack) {
        ValueEventListener userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG_USER_REPOSITORY, "Getting data from database on " + uid);
                User user = dataSnapshot.child(uid).getValue(User.class);
                callBack.callBack(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG_USER_REPOSITORY, databaseError.getMessage());
            }
        };

        this.dataBaseReference.addListenerForSingleValueEvent(userEventListener);
    }

    /**
     * Call this to update a user in the database
     */
    public void updateUserProfile(String uid, User user) {
        Map<String, Object> userValues = user.toMap();
        this.dataBaseReference.child(uid).updateChildren(userValues);
    }

    public void updateUserAppointment(final Appointment appointment) {
        userReference.child("appointments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment current = snapshot.getValue(Appointment.class);
                    if (current.equals(appointment)) {
                        Log.i(TAG_USER_REPOSITORY, "Updating user appointment " + snapshot.getKey());
                        userReference.child("appointments").child(snapshot.getKey()).updateChildren(appointment.toMap());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG_USER_REPOSITORY, "error on data retrieval " + databaseError.getMessage());
            }
        });
    }


}