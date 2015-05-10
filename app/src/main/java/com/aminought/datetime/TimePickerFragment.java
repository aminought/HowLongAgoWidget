package com.aminought.datetime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int vId, hour, minute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Bundle args = this.getArguments();
        int tmpHour = args.getInt("hour");
        int tmpMinute = args.getInt("minute");
        vId = args.getInt("view_id");

        final Calendar c = Calendar.getInstance();
        hour = tmpHour==-1 ? c.get(Calendar.HOUR_OF_DAY) : tmpHour;
        minute = tmpMinute==-1 ? c.get(Calendar.MONTH) : tmpMinute;
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView showTimePickerButton = (TextView) getActivity().findViewById(vId);
        Bundle args = getArguments();
        args.putInt("hour", hourOfDay);
        args.putInt("minute", minute);
        Calendar timeCal = new GregorianCalendar(0, 0, 0, hourOfDay, minute);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        showTimePickerButton.setText(timeFormat.format(timeCal.getTime()));
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hour = args.getInt("hour");
        minute = args.getInt("minute");
        vId = args.getInt("view_id");
    }
}
