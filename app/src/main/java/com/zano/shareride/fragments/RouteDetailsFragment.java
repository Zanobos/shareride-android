package com.zano.shareride.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zano.shareride.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zano on 17/07/2017, 18:59.
 */

public class RouteDetailsFragment extends DialogFragment {

    @BindView(R.id.fragment_route_details_numberpicker) EditText seatsPicker;
    @BindView(R.id.fragment_route_details_datepicker) Button datePicker;
    @BindView(R.id.fragment_route_details_timepicker) Button timePicker;

    private RouteDetailsFragmentListener mListener;

    private Integer hourOfDay;
    private Integer minute;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Integer numberOfSeats;

    // Override the Fragment.onAttach() method to instantiate the RouteDetailsFragmentListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the RouteDetailsFragmentListener so we can send events to the host
            mListener = (RouteDetailsFragmentListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement RouteDetailsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_route_details_relative, null);
        ButterKnife.bind(this, view);
        builder.setView(view);

        final Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        minute = now.get(Calendar.MINUTE);
        numberOfSeats = 1;

        builder.setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogPositiveClick(numberOfSeats,year,month,dayOfMonth,hourOfDay,minute);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogNegativeClick();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(new TimePickerFragment.TimePickerListener() {
                    @Override
                    public void timePicked(int hourOfDay, int minute) {
                        RouteDetailsFragment.this.hourOfDay = hourOfDay;
                        RouteDetailsFragment.this.minute = minute;
                    }
                }, now);
                timePickerFragment.show(getActivity().getSupportFragmentManager(), "TimePickerFragment");
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment timePickerFragment = DatePickerFragment.newInstance(new DatePickerFragment.DatePickerListener() {
                    @Override
                    public void datePicked(int year, int month, int dayOfMonth) {
                        RouteDetailsFragment.this.year = year;
                        RouteDetailsFragment.this.month = month;
                        RouteDetailsFragment.this.dayOfMonth = dayOfMonth;
                    }
                }, now);
                timePickerFragment.show(getActivity().getSupportFragmentManager(), "DatePickerFragment");
            }
        });

        seatsPicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String seats = seatsPicker.getText().toString();
                if(!seats.isEmpty()) {
                    numberOfSeats = Integer.parseInt(seats);
                }
            }
        });

        return builder.create();
    }

    public interface RouteDetailsFragmentListener {
        void onDialogPositiveClick(int numberOfSeats, int year, int month, int dayOfMonth, int hourOfDay, int minute);
        void onDialogNegativeClick();
    }

}
