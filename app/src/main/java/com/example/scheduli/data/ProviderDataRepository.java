package com.example.scheduli.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.scheduli.data.fireBase.ProviderDataBaseCallback;
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

    public void getProviderByUid(final String uid, final ProviderDataBaseCallback callback) {
        ValueEventListener providerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG_PROVIDER_REPOSITORY, "Getting data from database on " + uid);
                Provider provider = dataSnapshot.child(uid).getValue(Provider.class);
                callback.onCallBack(provider);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG_PROVIDER_REPOSITORY, databaseError.getMessage());
            }
        };

        this.dataBaseReference.addListenerForSingleValueEvent(providerListener);
    }


}
