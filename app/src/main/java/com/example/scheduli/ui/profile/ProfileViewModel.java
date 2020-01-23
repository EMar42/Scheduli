package com.example.scheduli.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.scheduli.data.Provider;
import com.example.scheduli.data.User;
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.example.scheduli.data.repositories.ProviderDataRepository;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.utils.UsersUtils;


public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private static final String PROFILEVM_TAG = "Profile View Model Fragment";
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Provider> providerMutableLiveData;



    public ProfileViewModel() {
        super();
        Log.i(PROFILEVM_TAG, "Got inside ProfileViewModel ");
        userMutableLiveData = new MutableLiveData<>();
        UserDataRepository.getInstance().getUserFromUid(UsersUtils.getInstance().getCurrentUserUid(), new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                Log.i(PROFILEVM_TAG, "Got inside user data repository callback");
                User user = (User) object;
                userMutableLiveData.setValue(user);
            }
        });
        providerMutableLiveData = new MutableLiveData<>();
        ProviderDataRepository.getInstance().getProviderByUid(UsersUtils.getInstance().getCurrentUserUid(), new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                Log.i(PROFILEVM_TAG, "Got inside provider data repository callback");
                Provider provider = (Provider) object;
                providerMutableLiveData.setValue(provider);
            }
        });
    }


    public LiveData<User> getUserProfileData() {
        return userMutableLiveData;
    }

    public LiveData<Provider> getProviderProfileData(){
        return providerMutableLiveData;
    }

}
