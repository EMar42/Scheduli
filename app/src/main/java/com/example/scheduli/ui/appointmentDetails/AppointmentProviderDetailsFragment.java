package com.example.scheduli.ui.appointmentDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduli.R;

public class AppointmentProviderDetailsFragment extends Fragment {

    private AppointmentDetailsViewModel mViewModel;

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
        // TODO: Use the ViewModel
    }

}
