package com.example.scheduli.ui.viewAppointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;
import com.google.firebase.database.DataSnapshot;

public class AppointmentFragment extends Fragment {

    private AppointmentViewModel mViewModel;
    private ToggleButton modeButton;
    private ViewAppointmentsListAdapter adapter;
    private TextView noAppontmentsTextView;
    private RecyclerView appointmentRecyclerView;
    private TextView noAppointemtsTextViewDescription;

    public static AppointmentFragment newInstance() {
        return new AppointmentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.appointment_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);

        LiveData<DataSnapshot> liveData = mViewModel.getAllAppointments();
        liveData.observe(this.getViewLifecycleOwner(), new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                adapter.setAppointmentList(dataSnapshot);
                if (adapter.getAppointemntsCount() > 0) {
                    appointmentRecyclerView.setVisibility(View.VISIBLE);
                    noAppontmentsTextView.setVisibility(View.GONE);
                    noAppointemtsTextViewDescription.setVisibility(View.GONE);
                } else {
                    appointmentRecyclerView.setVisibility(View.GONE);
                    noAppontmentsTextView.setVisibility(View.VISIBLE);
                    noAppointemtsTextViewDescription.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFragmentView(view);

        adapter = new ViewAppointmentsListAdapter(view.getContext());
        appointmentRecyclerView.setAdapter(adapter);
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        modeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.getFilter().filter("future");
                } else {
                    adapter.getFilter().filter("past");
                }
            }
        });
    }


    private void initFragmentView(@NonNull View view) {
        modeButton = view.findViewById(R.id.tb_change_appointment_time);
        noAppontmentsTextView = view.findViewById(R.id.tv_appointment_no_show);
        noAppointemtsTextViewDescription = view.findViewById(R.id.tv_appointment_no_show_description);
        appointmentRecyclerView = view.findViewById(R.id.appointments_recycler_main);
    }


}
