package com.example.scheduli.ui.viewAppointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduli.R;
import com.example.scheduli.data.joined.JoinedAppointment;
import com.example.scheduli.utils.TriggerCallback;

import java.util.ArrayList;
import java.util.List;

public class AppointmentFragment extends Fragment {

    private AppointmentViewModel mViewModel;
    private ViewAppointmentsListAdapter adapter;
    private TextView noAppointmentsTextView;
    private RecyclerView appointmentRecyclerView;
    private TextView noAppointeesTextViewDescription;
    private RadioGroup filterGroup;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.appointment_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getParentFragment()).get(AppointmentViewModel.class);

        mViewModel.getAllJoinedAppointments().observe(this, new Observer<ArrayList<JoinedAppointment>>() {
            @Override
            public void onChanged(ArrayList<JoinedAppointment> joinedAppointments) {
                List<JoinedAppointment> currentAppointments = adapter.getJoinedAppointments();
                DiffUtil.DiffResult appointmentResult = DiffUtil.calculateDiff(new AppointmentDiffCallback(currentAppointments, joinedAppointments));
                adapter.setJoinedAppointments(joinedAppointments);
                appointmentResult.dispatchUpdatesTo(adapter);
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragmentView(view);

        if (adapter == null) {
            setRecyclerViewAndAdapter(view);
        }

        setFilterGroupClick();

    }

    private void setRecyclerViewAndAdapter(@NonNull View view) {
        adapter = new ViewAppointmentsListAdapter(view.getContext(), new TriggerCallback() {
            @Override
            public void onCallback() {
                appointmentRecyclerView.setVisibility(View.VISIBLE);
                filterGroup.setVisibility(View.VISIBLE);
                noAppointmentsTextView.setVisibility(View.GONE);
                noAppointeesTextViewDescription.setVisibility(View.GONE);
            }
        });

        if (adapter.getItemCount() == 0) {
            appointmentRecyclerView.setVisibility(View.GONE);
            filterGroup.setVisibility(View.GONE);
            noAppointmentsTextView.setVisibility(View.VISIBLE);
            noAppointeesTextViewDescription.setVisibility(View.VISIBLE);
        }

        appointmentRecyclerView.setAdapter(adapter);
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setFilterGroupClick() {
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

    @Override
    public void onResume() {
        super.onResume();
        filterGroup.check(R.id.appointment_radio_filter_all);
    }
}
