package com.example.scheduli.ui.profile;

import android.telecom.Call;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.scheduli.data.User;
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.utils.UsersUtils;


public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<User> mutableLiveData;


    public ProfileViewModel() {
        super();
        mutableLiveData = new MutableLiveData<>();
        UserDataRepository.getInstance().getUserFromUid(UsersUtils.getInstance().getCurrentUserUid(), new DataBaseCallBackOperation() {
            @Override
            public void callBack(Object object) {
                User user = (User) object;
                mutableLiveData.setValue(user);
            }
        });
    }


    public LiveData<User> getUserProfileData() {
        return mutableLiveData;
    }
}
