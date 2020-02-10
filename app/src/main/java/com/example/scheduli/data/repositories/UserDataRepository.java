package com.example.scheduli.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.scheduli.data.Appointment;
import com.example.scheduli.data.User;
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
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
    private int limitAmountofAppointments;

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
        this.limitAmountofAppointments = 100;
    }

    public void keepInSync(String uid) {
        userReference = this.dataBaseReference.child(uid);
        userReference.keepSynced(true);

    }


    //Get the appointments of the user
    //just pass call back operation to receive the appointment list from FireBase.
    public void getUserAppointments(final DataBaseCallBackOperation callBackOperation) {
        if (appointmentListener == null) {
            appointmentListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Appointment> appointments = new ArrayList<>();
                    Iterable<DataSnapshot> appoinmentIndex = dataSnapshot.getChildren();
                    for (DataSnapshot appointmentData : appoinmentIndex) {
                        Appointment appointment = appointmentData.getValue(Appointment.class);
                        appointments.add(appointment);
                    }

                    callBackOperation.callBack(appointments);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG_USER_REPOSITORY, "Error cannot get data " + databaseError.getMessage());
                }
            };

            userReference.child("appointments").orderByChild("start").limitToLast(limitAmountofAppointments).addValueEventListener(appointmentListener);
        }
    }

    public void createNewUserInApp(String uid, User user) {
        Log.i(TAG_USER_REPOSITORY, "Created new User " + user);
        dataBaseReference.child(uid).setValue(user);
    }

    public void addAppointmentsListToUser(String uid, ArrayList<Appointment> appointments) {
        dataBaseReference.child(uid).child("appointments").setValue(appointments);
    }

    public void addSingleAppointmentToUser(final String uid, final Appointment appointment) {
        final DataBaseCallBackOperation callBackOperation = new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                Integer index = (Integer) object;

                Log.i(TAG_USER_REPOSITORY, "added appointment to user " + uid + " at index " + index);
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
     * Pass the user UID from user utils, pass a callback action to the method, in it you will get the user value returned from FireBase.
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
     * Just pass in the UID and the user to update.
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


    //On logout clear all the event listeners from appointments of the user.
    public void clearEventsOfAppointments() {
        if (this.appointmentListener != null) {
            userReference.child("appointments").removeEventListener(this.appointmentListener);
            this.appointmentListener = null;
        }
    }

    // get the limit from the shared preferences for the number of appointments
    public void setLimitAmountOfAppointments(int limitAmountofAppointments) {
        if (limitAmountofAppointments != this.limitAmountofAppointments) {
            Log.i(TAG_USER_REPOSITORY, "Setting appointment limit " + limitAmountofAppointments);
            //Set to maximum if no limit is set from settings
            if (limitAmountofAppointments == 0)
                limitAmountofAppointments = 100;

            this.limitAmountofAppointments = limitAmountofAppointments;
            clearEventsOfAppointments();
        }
    }

    public int getLimitAmountOfAppointments() {
        return limitAmountofAppointments;
    }

    //Remove an appointment from the database.
    //Pass the user uid and the updated appointment list.
    //pass the removed appointment to update the provider as well
    public void deleteAppointments(String currentUserUid, ArrayList<Appointment> appointments, Appointment appointment) {
        Log.i(TAG_USER_REPOSITORY, "deleteing appointment from " + currentUserUid + " appointment: " + appointment);
        this.dataBaseReference.child(currentUserUid).child("appointments").setValue(appointments);

        Calendar appointmentStartTime = Calendar.getInstance();
        appointmentStartTime.setTimeInMillis(appointment.getStart());
        if (Calendar.getInstance().before(appointmentStartTime)) {
            ProviderDataRepository.getInstance().changeSessionStatusFromByUser(appointment.getProviderUid(), appointment);
        }
    }

    // This deletes an appointment from a user if the start time is not after the current date time of current calendar
    // Just pass user uid , the provider uid, and the start time for the user.
    public void deleteAppointmentFromUser(final String userUid, final String providerUid, final long start) {
        Calendar current = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        System.out.println(current.getTimeInMillis());
        startDate.setTimeInMillis(start);
        final DataBaseCallBackOperation callBackOperation = new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                ArrayList<Appointment> appointments = (ArrayList<Appointment>) object;
                ArrayList<Appointment> editedAppointments = new ArrayList<>();

                for (Appointment appointment : appointments) {
                    if (!(appointment.getProviderUid().equals(providerUid) && appointment.getStart() == start)) {
                        editedAppointments.add(appointment);
                    }
                }

                UserDataRepository.getInstance().addAppointmentsListToUser(userUid, editedAppointments);
            }
        };

        if (current.before(startDate)) {
            this.dataBaseReference.child(userUid).child("appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i(TAG_USER_REPOSITORY, "Getting user " + userUid + " appointments for updating");
                    ArrayList<Appointment> appointments = new ArrayList<>();
                    Iterable<DataSnapshot> appoinmentIndex = dataSnapshot.getChildren();
                    for (DataSnapshot appointmentData : appoinmentIndex) {
                        Appointment appointment = appointmentData.getValue(Appointment.class);
                        appointments.add(appointment);
                    }

                    callBackOperation.callBack(appointments);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG_USER_REPOSITORY, "Failed with appointment updating data base error " + databaseError);
                }
            });
        }


    }
}
