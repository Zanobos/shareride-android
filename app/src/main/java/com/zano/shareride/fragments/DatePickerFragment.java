package com.zano.shareride.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Zano on 17/07/2017, 19:51.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerListener mListener;
    private Calendar startingTime;

    public static DatePickerFragment newInstance(DatePickerListener listener, @Nullable Calendar startingTime) {

        DatePickerFragment fragment = new DatePickerFragment();
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
        int year = startingTime.get(Calendar.YEAR);
        int month = startingTime.get(Calendar.MONTH);
        int day = startingTime.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (mListener != null)
            mListener.datePicked(year, month, dayOfMonth);
    }

    interface DatePickerListener {
        void datePicked(int year, int month, int dayOfMonth);
    }

}
