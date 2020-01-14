package com.example.scheduli.ui.viewAppointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;
import com.example.scheduli.data.Appointment;
import com.example.scheduli.data.Provider;
import com.example.scheduli.data.ProviderDataRepository;
import com.example.scheduli.data.fireBase.ProviderDataBaseCallback;
import com.example.scheduli.data.joined.JoinedAppointment;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class AppointmentFragment extends Fragment {

    private AppointmentViewModel mViewModel;
    private ViewAppointmentsListAdapter adapter;
    private TextView noAppointmentsTextView;
    private RecyclerView appointmentRecyclerView;
    private TextView noAppointeesTextViewDescription;
    private RadioButton filterAllButton;
    private RadioButton filterFutureButton;
    private RadioButton filterPastButton;
    private RadioGroup filterGroup;


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
                ArrayList<Appointment> appointments = new ArrayList<>();
                Iterable<DataSnapshot> appoinmentIndex = dataSnapshot.getChildren();
                for (DataSnapshot appointmentData : appoinmentIndex) {
                    Appointment appointment = appointmentData.getValue(Appointment.class);
                    appointments.add(appointment);
                }

                for (final Appointment appointment : appointments) {
                    ProviderDataRepository.getInstance().getProviderByUid(appointment.getProviderUid(), new ProviderDataBaseCallback() {
                        @Override
                        public void onCallBack(Provider provider) {
                            JoinedAppointment joinedAppointment = new JoinedAppointment(appointment, provider.getImageUrl(), provider.getCompanyName()
                                    , provider.getProfession(), provider.getPhoneNumber(), provider.getAddress());
                            adapter.addJoinedAppointment(joinedAppointment);
                        }
                    });
                }


            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragmentView(view);

        adapter = new ViewAppointmentsListAdapter(view.getContext(), new TriggerCallback() {
            @Override
            public void onCallback() {
                if (adapter.getItemCount() > 0) {
                    appointmentRecyclerView.setVisibility(View.VISIBLE);
                    filterGroup.setVisibility(View.VISIBLE);
                    noAppointmentsTextView.setVisibility(View.GONE);
                    noAppointeesTextViewDescription.setVisibility(View.GONE);
                } else {
                    appointmentRecyclerView.setVisibility(View.GONE);
                    filterGroup.setVisibility(View.INVISIBLE);
                    noAppointmentsTextView.setVisibility(View.VISIBLE);
                    noAppointeesTextViewDescription.setVisibility(View.VISIBLE);
                }
            }
        });
        appointmentRecyclerView.setAdapter(adapter);
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        filterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.appointment_radio_filter_future:
                        adapter.getFilter().filter("future");
                        break;
                    case R.id.appointment_radio_filter_past:
                        adapter.getFilter().filter("past");
                        break;
                    case R.id.appointment_radio_filter_all:
                    default:
                        adapter.getFilter().filter("");
                }
            }
        });

    }


    private void initFragmentView(@NonNull View view) {
        noAppointmentsTextView = view.findViewById(R.id.tv_appointment_no_show);
        noAppointeesTextViewDescription = view.findViewById(R.id.tv_appointment_no_show_description);
        appointmentRecyclerView = view.findViewById(R.id.appointments_recycler_main);
        filterGroup = view.findViewById(R.id.appointment_filter_radio_group);
    }


}
