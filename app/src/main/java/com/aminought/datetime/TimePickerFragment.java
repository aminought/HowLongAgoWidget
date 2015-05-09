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

    private int vId;
    private int idDTCS;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Bundle args = this.getArguments();

        int hour = args.getInt("hour");
        int minute = args.getInt("minute");
        vId = args.getInt("view_id");
        idDTCS = args.getInt("idDTCS");

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DateTimeCurrentState.hour[idDTCS] = hourOfDay;
        DateTimeCurrentState.minute[idDTCS] = minute;

        TextView showTimePickerButton = (TextView) getActivity().findViewById(vId);
        Calendar timeCal = new GregorianCalendar(0, 0, 0, DateTimeCurrentState.hour[idDTCS],
                                                 DateTimeCurrentState.minute[idDTCS]);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        showTimePickerButton.setText(timeFormat.format(timeCal.getTime()));
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        DateTimeCurrentState.hour[idDTCS] = args.getInt("hour");
        DateTimeCurrentState.minute[idDTCS] = args.getInt("minute");
        vId = args.getInt("view_id");
        idDTCS = args.getInt("idDTCS");
    }
}
