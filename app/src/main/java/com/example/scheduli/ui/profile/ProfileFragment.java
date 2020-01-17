package com.example.scheduli.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduli.R;
import com.example.scheduli.data.User;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private LiveData<User> user;
    private ImageView userProfilePictureIv;
    private TextView userProfileNameTv;
    private TextView userFullNameTv;
    private TextView userPhoneNumberTv;
    private TextView userEmailTv;
    private Button providerButton;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getParentFragment()).get(ProfileViewModel.class);
        mViewModel.getUserProfileData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userProfileNameTv.setText(user.getUserName());
                userFullNameTv.setText(user.getFullName());
                userPhoneNumberTv.setText(user.getPhoneNumber());
                userEmailTv.setText(user.getEmail());
                //TODO: Fix picture
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragmentView(view);
    }


    private void initFragmentView(@NonNull View view) {
        userProfileNameTv = view.findViewById(R.id.tv_username);
        userFullNameTv = view.findViewById(R.id.tv_user_full_name);
        userPhoneNumberTv = view.findViewById(R.id.tv_user_phonenumber);
        userEmailTv = view.findViewById(R.id.tv_user_email);

    }
}
