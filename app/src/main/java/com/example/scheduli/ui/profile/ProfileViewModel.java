package com.example.scheduli.ui.profile;

import android.service.autofill.UserData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.scheduli.data.repositories.UserDataRepository;
import com.google.firebase.database.DataSnapshot;

public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private LiveData<DataSnapshot> userProfileData;

    public ProfileViewModel(){
        super();
        //userProfileData = UserDataRepository.getInstance().getUserFromUid();
    }

    public LiveData<DataSnapshot> getUserData() {return userProfileData;}




}
