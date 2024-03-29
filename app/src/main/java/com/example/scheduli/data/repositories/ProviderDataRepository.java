package com.example.scheduli.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.scheduli.data.Appointment;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.Sessions;
import com.example.scheduli.data.WorkDay;
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.example.scheduli.data.fireBase.FirebaseQueryLiveData;
import com.example.scheduli.utils.UsersUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties

public class ProviderDataRepository {
    private static final String TAG_PROVIDER_REPOSITORY = "Provider repository";
    private static ProviderDataRepository instance;
    private final DatabaseReference dataBaseReference;
    private FirebaseQueryLiveData providersLiveData;
    private DateFormat sessionFormat = new SimpleDateFormat("yyyy-MM-dd");


    public static ProviderDataRepository getInstance() {
        if (instance == null) {
            synchronized (ProviderDataRepository.class) {
                if (instance == null) {
                    instance = new ProviderDataRepository();
                }
            }
        }
        return instance;
    }

    private ProviderDataRepository() {
        this.dataBaseReference = FirebaseDatabase.getInstance().getReference("providers");
    }

    public void createNewProviderInApp(String uid, Provider provider) {
        Log.i(TAG_PROVIDER_REPOSITORY, "Created new Provider " + provider);
        dataBaseReference.child(uid).setValue(provider);
    }

    /*
     * Use this if you want to retrive a single provider from the database without any more updates.
     * Provide Uid and a callback interface to receive the values in your caller.
     * */
    public void getProviderByUid(final String uid, final DataBaseCallBackOperation callback) {
        final ValueEventListener providerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    ArrayList<Service> services = new ArrayList<>();

                    Log.i(TAG_PROVIDER_REPOSITORY, "Getting data from database on " + uid);
                    //Provider provider = dataSnapshot.child(uid).getValue(Provider.class);
                    DataSnapshot providerSnapshot = dataSnapshot.child(uid);
                    if (providerSnapshot.getValue() != null) {
                        for (DataSnapshot snapshot : providerSnapshot.child("services").getChildren()) {
                            Service service = new Service();
                            service.setCost(Float.valueOf(snapshot.child("cost").getValue().toString()));
                            service.setDailySessions((Map<String, ArrayList<Sessions>>) snapshot.child("dailySessions").getValue());
                            service.setName(snapshot.child("name").getValue().toString());
                            service.setSingleSessionInMinutes(Integer.valueOf(snapshot.child("singleSessionInMinutes").getValue().toString()));

                            HashMap<String, WorkDay> workDays = new HashMap<>();

                            for (DataSnapshot ds : snapshot.child("workingDays").getChildren()) {
                                String key = ds.getKey();
                                WorkDay day = ds.getValue(WorkDay.class);
                                workDays.put(key, day);
                            }
                            service.setWorkingDays(workDays);

                            services.add(service);
                        }

                        Provider provider = new Provider();
                        Object image = providerSnapshot.child("imageUrl").getValue();
                        if (image == null) {
                            provider.setImageUrl("");
                        } else {
                            provider.setImageUrl(image.toString());
                        }
                        provider.setProfession(providerSnapshot.child("profession").getValue().toString());
                        provider.setCompanyName(providerSnapshot.child("companyName").getValue().toString());
                        provider.setPhoneNumber(providerSnapshot.child("phoneNumber").getValue().toString());
                        provider.setAddress(providerSnapshot.child("address").getValue().toString());
                        provider.setServices(services);

                        callback.callBack(provider);
                    } else {
                        callback.callBack(null);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG_PROVIDER_REPOSITORY, databaseError.getMessage());
            }
        };

        this.dataBaseReference.addListenerForSingleValueEvent(providerListener);
    }

    /**
     * Gets you the datasnapshot of providers table, use observe on the returned value
     * and in it do the required data manipulation to get the list of providers
     */
    public LiveData<DataSnapshot> getAllProviders() {
        this.providersLiveData = new FirebaseQueryLiveData(this.dataBaseReference);
        return providersLiveData;
    }


    public void setProviderServices(String uid, ArrayList<Service> services) {
        int index = 0;

        for (Service service : services) {
            dataBaseReference.child(uid).child("services").child(Integer.toString(index++)).setValue(service);
        }

        Log.d(TAG_PROVIDER_REPOSITORY, "Services updated. " + uid);
    }

    public void setSingleAppointmentValue(String provider_id, int serviceIndex, Date date, int sessionIndex, Sessions session) {
        Log.i(TAG_PROVIDER_REPOSITORY, "Updated session " + session);

         dataBaseReference.child(provider_id).child("services").child(String.valueOf(serviceIndex)).child("dailySessions").child(sessionFormat.format(date)).child(String.valueOf(sessionIndex)).setValue(session);

    }

    public void changeSessionStatusFromByUser(final String providerUID, final Appointment appointment) {
        Log.i(TAG_PROVIDER_REPOSITORY, "Changing appointment status in provider");

        dataBaseReference.child(providerUID).child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service currentService = snapshot.getValue(Service.class);

                    if (currentService.getName().equals(appointment.getServiceName())) {
                        String serviceKey = snapshot.getKey();
                        for (HashMap.Entry<String, ArrayList<Sessions>> entry : currentService.getDailySessions().entrySet()) {
                            for (int entryIndex = 0; entryIndex < entry.getValue().size(); entryIndex++) {
                                if (entry.getValue().get(entryIndex) != null && entry.getValue().get(entryIndex).getStart() == appointment.getStart() && entry.getValue().get(entryIndex).getUserUid().equals(UsersUtils.getInstance().getCurrentUserUid())) {
                                    entry.getValue().get(entryIndex).setAvailable(true);
                                    entry.getValue().get(entryIndex).setUserUid("No one");
                                    dataBaseReference.child(providerUID).child("services").child(serviceKey).child("dailySessions").child(entry.getKey()).child(Integer.toString(entryIndex)).setValue(entry.getValue().get(entryIndex));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG_PROVIDER_REPOSITORY, "Data base error " + databaseError.getMessage());
            }
        });
    }

    // insert a new Service to a provider to use.
    public void insertServiceToProvider(final String providerUid, final Service newService) {
        final DataBaseCallBackOperation insertService = new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                ArrayList<Service> services;

                if (object != null) {
                    services = (ArrayList<Service>) object;
                } else {
                    services = new ArrayList<>();
                }

                services.add(newService);
                ProviderDataRepository.getInstance().setProviderServices(providerUid, services);
            }
        };

        this.dataBaseReference.child(providerUid).child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Service> services = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = new Service();
                    service.setCost(Float.valueOf(snapshot.child("cost").getValue().toString()));
                    service.setDailySessions((Map<String, ArrayList<Sessions>>) snapshot.child("dailySessions").getValue());
                    service.setName(snapshot.child("name").getValue().toString());
                    service.setSingleSessionInMinutes(Integer.valueOf(snapshot.child("singleSessionInMinutes").getValue().toString()));

                    HashMap<String, WorkDay> workDays = new HashMap<>();

                    for (DataSnapshot ds : snapshot.child("workingDays").getChildren()) {
                        String key = ds.getKey();
                        WorkDay day = ds.getValue(WorkDay.class);
                        workDays.put(key, day);
                    }
                    service.setWorkingDays(workDays);

                    services.add(service);
                }

                insertService.callBack(services);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG_PROVIDER_REPOSITORY, "Problem with database operation on service list fetch " + databaseError);
            }
        });
    }

}



