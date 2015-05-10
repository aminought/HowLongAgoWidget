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

    private int vId, year, month, day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        int tmpYear = args.getInt("year");
        int tmpMonth = args.getInt("month");
        int tmpDay = args.getInt("day");
        vId = args.getInt("view_id");

        final Calendar c = Calendar.getInstance();
        year = tmpYear==-1 ? c.get(Calendar.YEAR) : tmpYear;
        month = tmpMonth==-1 ? c.get(Calendar.MONTH) : tmpMonth;
        day = tmpDay==-1 ? c.get(Calendar.DAY_OF_MONTH) : tmpDay;

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView showDatePickerButton = (TextView) getActivity().findViewById(vId);
        Bundle args = getArguments();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        Calendar dateCal = new GregorianCalendar(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        showDatePickerButton.setText(dateFormat.format(dateCal.getTime()));
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        vId = args.getInt("view_id");
    }
}
