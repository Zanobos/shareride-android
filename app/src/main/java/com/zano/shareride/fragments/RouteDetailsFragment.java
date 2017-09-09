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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zano.shareride.R;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

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
    @BindView(R.id.fragment_route_details_cb_deliverytime) CheckBox deliveryTimeCheckBox;

    @BindView(R.id.fragment_route_details_tv_deliverytime) TextView deliveryTimeTextView;

    private RouteDetailsFragmentListener mListener;
    private Context mContext;

    private LocalDate date;
    private LocalTime time;
    private Integer numberOfSeats;
    private boolean deliveryTime;

    // Override the Fragment.onAttach() method to instantiate the RouteDetailsFragmentListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the RouteDetailsFragmentListener so we can send events to the host
            mListener = (RouteDetailsFragmentListener) context;
            mContext = context;
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
        date = new LocalDate();
        time = new LocalTime();
        numberOfSeats = 1;
        deliveryTime = true;

        builder.setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (validate()) {
                    mListener.onDialogPositiveClick(deliveryTime, numberOfSeats, date, time);
                } else {
                    Toast.makeText(mContext, R.string.input_not_valid, Toast.LENGTH_SHORT).show(); //TODO
                }
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
                        time = new LocalTime(hourOfDay, minute);
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
                        date = new LocalDate(year, month + 1, dayOfMonth); //Month is still 0-indexed
                    }
                }, now);
                timePickerFragment.show(getActivity().getSupportFragmentManager(), "DatePickerFragment");
            }
        });

        seatsPicker.setText("1");
        seatsPicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String seats = seatsPicker.getText().toString();
                if (!seats.isEmpty()) {
                    numberOfSeats = Integer.parseInt(seats);
                }
            }
        });

        deliveryTimeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryTime = deliveryTimeCheckBox.isChecked();
                if (!deliveryTime) {
                    deliveryTimeTextView.setText(R.string.fragment_route_details_pickuptime);
                } else {
                    deliveryTimeTextView.setText(R.string.fragment_route_details_deliverytime);
                }
            }
        });

        return builder.create();
    }

    //TODO
    private boolean validate() {
        boolean valid = true;

        return valid;
    }

    public interface RouteDetailsFragmentListener {
        void onDialogPositiveClick(boolean deliveryTime, int numberOfSeats, LocalDate date, LocalTime time);

        void onDialogNegativeClick();
    }

}
