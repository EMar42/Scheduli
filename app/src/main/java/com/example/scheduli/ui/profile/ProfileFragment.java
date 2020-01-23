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
import com.example.scheduli.ui.provider.ProviderActivity;
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
                //TODO: Fix picture
            }
        });
        mViewModel.getProviderProfileData().observe(this, new Observer<Provider>() {
            @Override
            public void onChanged(Provider provider) {
                if (provider == null) {
                    Log.i(PROFILEFRAGMENT, "True from if");
                }
                Log.i(PROFILEFRAGMENT, "Get in onChanged of getProviderProfileData ");
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
                if (true){
                    Log.i(PROFILEFRAGMENT, "clicked on providerButton");
                    //Intent intent = new Intent(getActivity(), ProviderActivity.class);
                    //startActivity(intent);
                    Toast.makeText(getContext(),"clicked succ",Toast.LENGTH_SHORT).show();
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

    }


}
