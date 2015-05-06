package com.aminought.hlawidget;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aminought.datetime.DatePickerFragment;
import com.aminought.datetime.DateTime;
import com.aminought.datetime.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class HLAActivity extends FragmentActivity implements View.OnClickListener {
    private DialogFragment newDateFragment = new DatePickerFragment();
    private DialogFragment newTimeFragment = new TimePickerFragment();
    private TextView showDatePicker1ActivityButton;
    private TextView showTimePicker1ActivityButton;
    private TextView showDatePicker2ActivityButton;
    private TextView showTimePicker2ActivityButton;
    private TextView differenceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hla);

        Button computeDifference = (Button) findViewById(R.id.computeDiffButton);
        computeDifference.setOnClickListener(this);

        showDatePicker1ActivityButton = (TextView) findViewById(R.id.showDatePicker1ActivityButton);
        showDatePicker1ActivityButton.setOnClickListener(this);
        showTimePicker1ActivityButton = (TextView) findViewById(R.id.showTimePicker1ActivityButton);
        showTimePicker1ActivityButton.setOnClickListener(this);
        showDatePicker2ActivityButton = (TextView) findViewById(R.id.showDatePicker2ActivityButton);
        showDatePicker2ActivityButton.setOnClickListener(this);
        showTimePicker2ActivityButton = (TextView) findViewById(R.id.showTimePicker2ActivityButton);
        showTimePicker2ActivityButton.setOnClickListener(this);
        differenceTextView = (TextView) findViewById(R.id.differenceTextView);

        TextView titleActivityTextView = (TextView) findViewById(R.id.titleActivityTextView);
        Typeface font = Typeface.createFromAsset(getAssets(), "BuxtonSketch.ttf");
        titleActivityTextView.setTypeface(font);

        Date now_date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        showDatePicker1ActivityButton.setText(dateFormat.format(now_date));
        showTimePicker1ActivityButton.setText(timeFormat.format(now_date));
        showDatePicker2ActivityButton.setText(dateFormat.format(now_date));
        showTimePicker2ActivityButton.setText(timeFormat.format(now_date));

        differenceTextView.setText("");
    }

    public void showDatePickerDialog(int vId) {
        Bundle dateArgs = new Bundle();

        TextView tv = (TextView) findViewById(vId);
        String dateString = tv.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        dateArgs.putInt("year", cal.get(Calendar.YEAR));
        dateArgs.putInt("month", cal.get(Calendar.MONTH));
        dateArgs.putInt("day", cal.get(Calendar.DAY_OF_MONTH));
        dateArgs.putInt("view_id", vId);
        newDateFragment.setArguments(dateArgs);
        newDateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(int vId) {
        Bundle timeArgs = new Bundle();

        TextView tv = (TextView) findViewById(vId);
        String timeString = tv.getText().toString();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        try {
            date = timeFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        timeArgs.putInt("hour", cal.get(Calendar.HOUR_OF_DAY));
        timeArgs.putInt("minute", cal.get(Calendar.MINUTE));
        timeArgs.putInt("view_id", vId);
        newTimeFragment.setArguments(timeArgs);
        newTimeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void computeDifference() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date d1 = new Date();
        Date d2 = new Date();
        try {
            d1 = dateFormat.parse(showDatePicker1ActivityButton.getText().toString() + " " +
                                  showTimePicker1ActivityButton.getText().toString());
            d2 = dateFormat.parse(showDatePicker2ActivityButton.getText().toString() + " " +
                                  showTimePicker2ActivityButton.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(d1);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(d2);
        DateTime dt1 = new DateTime(cal1);
        DateTime dt2 = new DateTime(cal2);
        DateTime dt3 = dt1.diff(dt2);
        differenceTextView.setText(Html.fromHtml(dt3.toString("#ffff00", "#ffffff", true)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showDatePicker1ActivityButton:
                showDatePickerDialog(R.id.showDatePicker1ActivityButton);
                break;
            case R.id.showDatePicker2ActivityButton:
                showDatePickerDialog(R.id.showDatePicker2ActivityButton);
                break;
            case R.id.showTimePicker1ActivityButton:
                showTimePickerDialog(R.id.showTimePicker1ActivityButton);
                break;
            case R.id.showTimePicker2ActivityButton:
                showTimePickerDialog(R.id.showTimePicker2ActivityButton);
                break;
            case R.id.computeDiffButton:
                computeDifference();
        }
    }
}
