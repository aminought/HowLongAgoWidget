package com.aminought.datetime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.aminought.hlawidget.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Bundle args = this.getArguments();

        int year = args.getInt("year");
        int month = args.getInt("month");
        int day = args.getInt("day");

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        DateTimeCurrentState.year = year;
        DateTimeCurrentState.month = month;
        DateTimeCurrentState.day = day;

        TextView showDatePickerButton = (TextView) getActivity()
                                        .findViewById(R.id.showDatePickerButton);
        Calendar dateCal = new GregorianCalendar(DateTimeCurrentState.year,
                                                 DateTimeCurrentState.month,
                                                 DateTimeCurrentState.day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        showDatePickerButton.setText(dateFormat.format(dateCal.getTime()));
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        DateTimeCurrentState.year = args.getInt("year");
        DateTimeCurrentState.month = args.getInt("month");
        DateTimeCurrentState.day = args.getInt("day");
    }
}
