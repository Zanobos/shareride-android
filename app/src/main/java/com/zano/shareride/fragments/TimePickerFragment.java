package com.zano.shareride.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Zano on 17/07/2017, 19:51.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerListener mListener;
    private Calendar startingTime;

    public static TimePickerFragment newInstance(TimePickerListener listener, @Nullable Calendar startingTime) {

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.mListener = listener;
        if(startingTime != null) {
            fragment.startingTime = startingTime;
        } else {
            fragment.startingTime = Calendar.getInstance();
        }
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        int hour = startingTime.get(Calendar.HOUR_OF_DAY);
        int minute = startingTime.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mListener != null)
            mListener.timePicked(hourOfDay, minute);
    }

    interface TimePickerListener {
        void timePicked(int hourOfDay, int minute);
    }

}
