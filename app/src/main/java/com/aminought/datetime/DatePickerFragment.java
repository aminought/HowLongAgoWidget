package com.aminought.datetime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private int vId;
    private int idDTCS;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Bundle args = this.getArguments();

        int year = args.getInt("year");
        int month = args.getInt("month");
        int day = args.getInt("day");
        vId = args.getInt("view_id");
        idDTCS = args.getInt("idDTCS");

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        DateTimeCurrentState.year[idDTCS] = year;
        DateTimeCurrentState.month[idDTCS] = month;
        DateTimeCurrentState.day[idDTCS] = day;

        TextView showDatePickerButton = (TextView) getActivity().findViewById(vId);
        Calendar dateCal = new GregorianCalendar(DateTimeCurrentState.year[idDTCS],
                                                 DateTimeCurrentState.month[idDTCS],
                                                 DateTimeCurrentState.day[idDTCS]);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        showDatePickerButton.setText(dateFormat.format(dateCal.getTime()));
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        DateTimeCurrentState.year[idDTCS] = args.getInt("year");
        DateTimeCurrentState.month[idDTCS] = args.getInt("month");
        DateTimeCurrentState.day[idDTCS] = args.getInt("day");
        vId = args.getInt("view_id");
        idDTCS = args.getInt("idDTCS");
    }
}
