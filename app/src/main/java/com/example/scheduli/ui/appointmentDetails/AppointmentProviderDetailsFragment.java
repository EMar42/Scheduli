package com.example.scheduli.ui.appointmentDetails;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduli.R;
import com.example.scheduli.utils.DownloadImageAsync;

public class AppointmentProviderDetailsFragment extends Fragment {

    //Fragment Controls
    private AppointmentDetailsViewModel mViewModel;
    private ImageView providerProfileImage;
    private TextView providerCompanyName;
    private TextView providerProfession;
    private TextView providerAddress;
    private TextView providerPhone;
    private Button callProviderButton;


    public static AppointmentProviderDetailsFragment newInstance() {
        return new AppointmentProviderDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.appointment_provider_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(AppointmentDetailsViewModel.class);

        if (mViewModel.getProviderImage() == null) {
            new DownloadImageAsync(providerProfileImage).execute(mViewModel.getJoinedAppointment().getProviderImageUrl());
        } else {
            providerProfileImage.setImageBitmap(mViewModel.getProviderImage());
        }

        providerCompanyName.setText(mViewModel.getJoinedAppointment().getProviderCompanyName());
        providerProfession.setText(mViewModel.getJoinedAppointment().getProviderProfession());
        providerPhone.setText(mViewModel.getJoinedAppointment().getProviderPhoneNumber());
        providerAddress.setText(mViewModel.getJoinedAppointment().getProviderAddress());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        callProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfEmpty(providerPhone)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + providerPhone.getText().toString()));
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getString(R.string.provider_missing_phone_error_message), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initView(@NonNull View view) {
        providerProfileImage = view.findViewById(R.id.iv_appointemtn_details_provider_image);
        providerCompanyName = view.findViewById(R.id.tv_provider_app_details_company_name);
        providerProfession = view.findViewById(R.id.tv_provider_app_details_proffesion);
        providerAddress = view.findViewById(R.id.tv_provider_app_details_address);
        providerPhone = view.findViewById(R.id.tv_provider_app_details_phone);
        callProviderButton = view.findViewById(R.id.btn_provider_app_details_call);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //Save image on navigation
        if (providerProfileImage.getDrawable() != null) {
            mViewModel.setProviderImage(((BitmapDrawable) providerProfileImage.getDrawable()).getBitmap());
        }
    }


    private boolean checkIfEmpty(TextView textView) {
        return textView.getText().toString().isEmpty();
    }
}
