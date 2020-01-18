package com.example.scheduli.ui.appointmentDetails;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.scheduli.NotificationPublisher;
import com.example.scheduli.R;
import com.example.scheduli.data.repositories.UserDataRepository;
import com.example.scheduli.ui.dialogs.DatePickerDialogFragment;
import com.example.scheduli.ui.dialogs.TimerPickerDialogFragment;
import com.example.scheduli.utils.TriggerCallback;
import com.example.scheduli.utils.UpcomingAppointmentNotification;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

public class AppointemtDetailsFragment extends Fragment {

    private static final String APPOINTMENT_TAG = "Appointment Details Fragment";
    private AppointmentDetailsViewModel mViewModel;

    //Fragment controls
    private TextView appointmentServiceName;
    private TextView appointmentDate;
    private TextView appointmentTime;
    private TextView appointmentServiceCost;
    private TextView alertTimeSet;
    private TextView alertDateSet;
    private Button setAlertTimeButton;
    private Button setAlertDateButton;
    private Button callAlertButton;
    private Button setAppointmentInCalenderButton;
    private Button cancelAppointmentButton;


    public static AppointemtDetailsFragment newInstance() {
        return new AppointemtDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.appointemt_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(AppointmentDetailsViewModel.class);

        mViewModel.setTimeCallback(new TriggerCallback() {
            @Override
            public void onCallback() {
                Log.i(APPOINTMENT_TAG, "Got Time info from activity");
                String timeString = DateFormat.getTimeInstance().format(mViewModel.getAlarmSetTimeFromDialog().getTime());
                alertTimeSet.setText(timeString);
            }
        });

        mViewModel.setDateCallback(new TriggerCallback() {
            @Override
            public void onCallback() {
                Log.i(APPOINTMENT_TAG, "Got Date info from activity");
                String dateString = DateFormat.getDateInstance().format(mViewModel.getAlarmSetDateFromDialog().getTime());
                alertDateSet.setText(dateString);
            }
        });

        //Insert values for the controls in the fragment
        AsyncTask<Void, Void, Void> setFieldsTas = new AsyncTask<Void, Void, Void>() {

            private String alarDate;
            private String alarmTime;
            private String cost;
            private String dateOfAppointment;
            private Calendar appointmentEnd;
            private Calendar appointmentStart;
            private String appointmentPeriod;

            @Override
            protected Void doInBackground(Void... voids) {
                Log.i(APPOINTMENT_TAG, "Starting to fill view from task");
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                cost = currencyFormat.format(Float.valueOf(mViewModel.getJoinedAppointment().getAppointment().getServiceCost()));

                appointmentStart = Calendar.getInstance();
                appointmentEnd = Calendar.getInstance();

                appointmentStart.setTimeInMillis(mViewModel.getJoinedAppointment().getAppointment().getStart());
                appointmentEnd.setTimeInMillis(mViewModel.getJoinedAppointment().getAppointment().getEnd());

                dateOfAppointment = DateFormat.getDateInstance().format(appointmentStart.getTime());

                appointmentPeriod = DateFormat.getTimeInstance().format(appointmentStart.getTime()) + " - " + DateFormat.getTimeInstance().format(appointmentEnd.getTime());

                if (mViewModel.getJoinedAppointment().getAppointment().getAlarmReminderTime() != 0) {
                    alarmTime = DateFormat.getDateTimeInstance().format(new Date(mViewModel.getJoinedAppointment().getAppointment().getAlarmReminderTime()));
                    alarDate = DateFormat.getDateInstance().format(new Date(mViewModel.getJoinedAppointment().getAppointment().getAlarmReminderTime()));

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                appointmentServiceCost.setText(cost);
                appointmentDate.setText(dateOfAppointment);
                appointmentServiceName.setText(mViewModel.getJoinedAppointment().getAppointment().getServiceName());
                appointmentTime.setText(appointmentPeriod);

                if (mViewModel.getJoinedAppointment().getAppointment().getAlarmReminderTime() != 0) {
                    alertTimeSet.setText(alarmTime);
                    alertDateSet.setText(alarDate);
                }
            }
        }.execute();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        //Time dialog
        setAlertTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimerPickerDialogFragment();
                timePicker.show(getFragmentManager(), "alarm picker");
            }
        });

        //Date Dialog
        setAlertDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerDialogFragment();
                datePicker.show(getFragmentManager(), "alarm picker");
            }
        });

        //Trigger alarm
        callAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfEmpty(alertTimeSet) && !checkIfEmpty(alertDateSet)) {
                    triggerAlarmNotification();
                }
            }
        });

    }

    private void triggerAlarmNotification() {
        Calendar alarmTimeAndDate = Calendar.getInstance();
        alarmTimeAndDate.setTime(mViewModel.getAlarmSetDateFromDialog().getTime());
        alarmTimeAndDate.set(Calendar.HOUR, mViewModel.getAlarmSetTimeFromDialog().get(Calendar.HOUR));
        alarmTimeAndDate.set(Calendar.MINUTE, mViewModel.getAlarmSetTimeFromDialog().get(Calendar.MINUTE));
        alarmTimeAndDate.set(Calendar.SECOND, 0);

        Calendar appointmentStartDateTime = Calendar.getInstance();
        appointmentStartDateTime.setTime(new Date(mViewModel.getJoinedAppointment().getAppointment().getStart()));

        if (alarmTimeAndDate.before(appointmentStartDateTime) && Calendar.getInstance().before(alarmTimeAndDate)) {
            NotificationCompat.Builder builder = UpcomingAppointmentNotification.createNotification(getActivity(), mViewModel.getJoinedAppointment());
            Notification notification = builder.build();

            Intent notificationIntent = new Intent(getActivity(), NotificationPublisher.class);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NotificationPublisher.NOTIFICATION_COUNT++);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mViewModel.getJoinedAppointment().getAppointment().setAlarmReminderTime(alarmTimeAndDate.getTimeInMillis());
            UserDataRepository.getInstance().updateUserAppointment(mViewModel.getJoinedAppointment().getAppointment());

            Log.i(APPOINTMENT_TAG, "Setting Alarm for " + alarmTimeAndDate.getTime().toString());
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeAndDate.getTimeInMillis(), pendingIntent);
            Toast.makeText(getContext(), "Alarm is set to " + alarmTimeAndDate.getTime().toString(), Toast.LENGTH_LONG).show();

            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.twin_bell_alarm_clock);
            mediaPlayer.start();
        } else {
            if (alarmTimeAndDate.after(appointmentStartDateTime))
                Toast.makeText(getContext(), "Alarm is not before the appointment start time", Toast.LENGTH_LONG).show();
            if (Calendar.getInstance().after(alarmTimeAndDate))
                Toast.makeText(getContext(), "We passed the time of the alarm already", Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkIfEmpty(TextView textView) {
        return textView.getText().toString().isEmpty();
    }

    private void initView(@NonNull View view) {
        appointmentServiceName = view.findViewById(R.id.tv_app_details_service_name);
        appointmentDate = view.findViewById(R.id.tv_app_details_appointment_date);
        appointmentTime = view.findViewById(R.id.tv_app_details_appointment_time);
        appointmentServiceCost = view.findViewById(R.id.tv_app_details_service_cost);
        alertDateSet = view.findViewById(R.id.tv_app_details_alarm_selected_date);
        alertTimeSet = view.findViewById(R.id.tv_app_details_alarm_selected_time);
        setAlertDateButton = view.findViewById(R.id.btn_app_details_alarm_set_date);
        setAlertTimeButton = view.findViewById(R.id.btn_app_details_alarm_set_time);
        setAppointmentInCalenderButton = view.findViewById(R.id.btn_app_details_set_appointment_in_calender);
        callAlertButton = view.findViewById(R.id.btn_app_details_alarm_activate);
        cancelAppointmentButton = view.findViewById(R.id.btn_app_details_cancel_appointment);
    }


}
