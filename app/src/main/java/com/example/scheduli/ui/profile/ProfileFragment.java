package com.example.scheduli.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.scheduli.data.fireBase.DataBaseCallBackOperation;
import com.example.scheduli.data.repositories.StorageRepository;
import com.example.scheduli.ui.provider.ProviderActivity;
import com.example.scheduli.ui.provider.ProviderSingUpActivity;
import com.example.scheduli.utils.UsersUtils;

public class ProfileFragment extends Fragment {

    private static final String PROFILEFRAGMENT = "Profile Fragment";
    private ProfileViewModel mViewModel;
    private Provider currentProvider;

    private ImageView userProfilePictureIv;
    private TextView userProfileNameTv;
    private TextView userFullNameTv;
    private TextView userPhoneNumberTv;
    private TextView userEmailTv;
    private Button providerButton;
    private boolean providerFlagCallBack;
    private float disableButtonColor = 0.5f;
    private float enableButtonColor = 1.0f;

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
        providerFlagCallBack = true;
        mViewModel = ViewModelProviders.of(getParentFragment()).get(ProfileViewModel.class);
        mViewModel.getUserProfileData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userProfileNameTv.setText(user.getUserName());
                userFullNameTv.setText(user.getFullName());
                userPhoneNumberTv.setText(user.getPhoneNumber());
                userEmailTv.setText(user.getEmail());
                StorageRepository.getInstance().downloadImageFromStorage(UsersUtils.getInstance().getCurrentUserUid(), user.getProfileImage(), new DataBaseCallBackOperation() {
                    @Override
                    public void callBack(Object object) {
                        try {
                            userProfilePictureIv.setImageBitmap((Bitmap) object);
                        } catch (Exception e) {
                            Log.e("Profile fragment", "Cannot donwload image from user reason " + e.getMessage());
                        }
                    }
                });
            }
        });
        mViewModel.getProviderProfileData().observe(this, new Observer<Provider>() {
            @Override
            public void onChanged(Provider provider) {
                Log.i(PROFILEFRAGMENT, "Callback return from getProviderProfileData result" + provider);
                currentProvider = provider;
                providerButton.setEnabled(true);
                providerButton.setAlpha(enableButtonColor);
                if (provider == null) {
                    providerButton.setText("Become Provider Today!");
                    providerFlagCallBack = false;
                }
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
                if (providerFlagCallBack) {
                    //already Provider
                    Intent intent = new Intent(getContext(), ProviderActivity.class);
                    intent.putExtra("provider",currentProvider);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Already Provider", Toast.LENGTH_SHORT).show();
                } else {
                    //singup
                    Log.i(PROFILEFRAGMENT, "clicked on providerButton");
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ProviderSingUpActivity.class);
                    startActivity(intent);
                    Log.i(PROFILEFRAGMENT, "Start successfully provider singup activity");
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
        providerButton.setAlpha(disableButtonColor);
        providerButton.setEnabled(false);
        userProfilePictureIv = view.findViewById(R.id.user_profile_picture);
    }
}
