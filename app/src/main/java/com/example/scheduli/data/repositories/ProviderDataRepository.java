package com.example.scheduli.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.scheduli.data.Provider;
import com.example.scheduli.data.Service;
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.example.scheduli.data.fireBase.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

@IgnoreExtraProperties

public class ProviderDataRepository {
    private static final String TAG_PROVIDER_REPOSITORY = "Provider repository";
    private static ProviderDataRepository instance;
    private final DatabaseReference dataBaseReference;
    private FirebaseQueryLiveData providersLiveData;

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
        //TODO: check if the provider exists
        dataBaseReference.child(uid).setValue(provider);
    }

    /*
     * Use this if you want to retrive a single provider from the database without any more updates.
     * Provide Uid and a callback interface to receive the values in your caller.
     * */
    public void getProviderByUid(final String uid, final DataBaseCallBackOperation callback) {
        ValueEventListener providerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG_PROVIDER_REPOSITORY, "Getting data from database on " + uid);
                Provider provider = dataSnapshot.child(uid).getValue(Provider.class);
                callback.callBack(provider);
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



    public void setServices(String uid, Service service){

        dataBaseReference.child(uid).child("services").setValue(service);
        Log.d(TAG_PROVIDER_REPOSITORY, "Services updated. " + service.getName());


    }



}



