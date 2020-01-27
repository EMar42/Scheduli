package com.example.scheduli.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduli.R;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.User;
import com.example.scheduli.ui.provider.ProviderSingUpActivity;
import com.google.android.material.snackbar.Snackbar;

public class ProfileFragment extends Fragment {

    private static final String PROFILEFRAGMENT = "Profile Fragment";

    private ProfileViewModel mViewModel;


    private ImageView userProfilePictureIv;
    private TextView userProfileNameTv;
    private TextView userFullNameTv;
    private TextView userPhoneNumberTv;
    private TextView userEmailTv;
    private Button providerButton;
    private Snackbar snackbar;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    //
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
                //TODO: Fix on click picture
            }
        });
        mViewModel.getProviderProfileData().observe(this, new Observer<Provider>() {
            @Override
            public void onChanged(Provider provider) {
                Log.i(PROFILEFRAGMENT, "Callback return from getProviderProfileData result" + provider );
//                providerButton.setAlpha(buttonEnableColor);
//                providerButton.setAlpha(buttonEnableColor);
                providerButton.setEnabled(true);
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragmentView(view);

        providerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO fix the true to check if null returned
                Toast.makeText(getContext(),"isEnabled " + providerButton.isEnabled() + " is Clickable" + providerButton.isClickable(), Toast.LENGTH_LONG).show();
                if (true) {
                    Log.i(PROFILEFRAGMENT, "clicked on providerButton");
                    try {
                        Intent intent = new Intent(getContext(), ProviderSingUpActivity.class);
                        startActivity(intent);
                        Log.i(PROFILEFRAGMENT, "Start successfully provider activity");
                    } catch (Exception e) {
                        System.err.println(e);
                        Log.i(PROFILEFRAGMENT, "Error is creating activity");
                    }
                }
            }
        });
    }


    private void initFragmentView(@NonNull View view) {
        userProfileNameTv = view.findViewById(R.id.tv_username);
        userFullNameTv = view.findViewById(R.id.tv_user_full_name);
        userPhoneNumberTv = view.findViewById(R.id.tv_user_phonenumber);
        userEmailTv = view.findViewById(R.id.tv_user_email);
        providerButton = view.findViewById(R.id.btn_profile_provider);
//        providerButton.setAlpha(buttonDisableButton);
        providerButton.setEnabled(false);
    }

    private void getProviderSingUp() {

    }


}
