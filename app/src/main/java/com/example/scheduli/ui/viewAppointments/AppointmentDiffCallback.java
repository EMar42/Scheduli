package com.example.scheduli.ui.viewAppointments;

import androidx.recyclerview.widget.DiffUtil;

import com.example.scheduli.data.joined.JoinedAppointment;

import java.util.List;

public class AppointmentDiffCallback extends DiffUtil.Callback {
    List<JoinedAppointment> oldList;
    List<JoinedAppointment> newList;


    public AppointmentDiffCallback(List<JoinedAppointment> oldList, List<JoinedAppointment> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getAppointment().equals(newList.get(newItemPosition).getAppointment());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
