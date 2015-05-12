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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class HLAActivity extends FragmentActivity implements View.OnClickListener {
    private DialogFragment firstDateFragment;
    private DialogFragment firstTimeFragment;
    private DialogFragment secondDateFragment;
    private DialogFragment secondTimeFragment;
    private TextView showDatePicker1ActivityButton;
    private TextView showTimePicker1ActivityButton;
    private TextView showDatePicker2ActivityButton;
    private TextView showTimePicker2ActivityButton;
    private TextView differenceTextView;
    private DateTime diffTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hlaactivity);

        firstDateFragment = new DatePickerFragment();
        firstTimeFragment = new TimePickerFragment();
        secondDateFragment = new DatePickerFragment();
        secondTimeFragment = new TimePickerFragment();

        Calendar cal = new GregorianCalendar();

        // Set current date
        Bundle date1Args = new Bundle();
        date1Args.putInt("year", cal.get(Calendar.YEAR));
        date1Args.putInt("month", cal.get(Calendar.MONTH));
        date1Args.putInt("day", cal.get(Calendar.DAY_OF_MONTH));
        firstDateFragment.setArguments(date1Args);
        Bundle date2Args = (Bundle) date1Args.clone();
        secondDateFragment.setArguments(date2Args);

        // Set current time
        Bundle time1Args = new Bundle();
        time1Args.putInt("hour", cal.get(Calendar.HOUR_OF_DAY));
        time1Args.putInt("minute", cal.get(Calendar.MINUTE));
        firstTimeFragment.setArguments(time1Args);
        Bundle time2Args = (Bundle) time1Args.clone();
        secondTimeFragment.setArguments(time2Args);

        Button computeDifference = (Button) findViewById(R.id.computeDiffButton);
        computeDifference.setOnClickListener(this);
        diffTime = new DateTime(0,0,0,0,0);

        showDatePicker1ActivityButton = (TextView) findViewById(R.id.showDatePicker1ActivityButton);
        showDatePicker1ActivityButton.setOnClickListener(this);
        showTimePicker1ActivityButton = (TextView) findViewById(R.id.showTimePicker1ActivityButton);
        showTimePicker1ActivityButton.setOnClickListener(this);
        showDatePicker2ActivityButton = (TextView) findViewById(R.id.showDatePicker2ActivityButton);
        showDatePicker2ActivityButton.setOnClickListener(this);
        showTimePicker2ActivityButton = (TextView) findViewById(R.id.showTimePicker2ActivityButton);
        showTimePicker2ActivityButton.setOnClickListener(this);
        differenceTextView = (TextView) findViewById(R.id.differenceTextView);
        differenceTextView.setText("");

        // Apply custom font for title
        TextView titleActivityTextView = (TextView) findViewById(R.id.titleActivityTextView);
        Typeface font = Typeface.createFromAsset(getAssets(), "BuxtonSketch.ttf");
        titleActivityTextView.setTypeface(font);

        // Write current time into text views
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        showDatePicker1ActivityButton.setText(dateFormat.format(cal.getTime()));
        showTimePicker1ActivityButton.setText(timeFormat.format(cal.getTime()));
        showDatePicker2ActivityButton.setText(dateFormat.format(cal.getTime()));
        showTimePicker2ActivityButton.setText(timeFormat.format(cal.getTime()));

        // Add ads
        AdView adView = (AdView) findViewById(R.id.ActivityAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void showDatePickerDialog(DialogFragment d, int textViewId) {
        Bundle dateArgs = d.getArguments();
        dateArgs.putInt("view_id", textViewId);
        d.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(DialogFragment d, int textViewId) {
        Bundle timeArgs = d.getArguments();
        timeArgs.putInt("view_id", textViewId);
        d.show(getSupportFragmentManager(), "timePicker");
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
        diffTime = dt1.diff(dt2);
        differenceTextView.setText(Html.fromHtml(diffTime.toString("#ffff00", "#ffffff", true)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showDatePicker1ActivityButton:
                showDatePickerDialog(firstDateFragment, R.id.showDatePicker1ActivityButton);
                break;
            case R.id.showDatePicker2ActivityButton:
                showDatePickerDialog(secondDateFragment, R.id.showDatePicker2ActivityButton);
                break;
            case R.id.showTimePicker1ActivityButton:
                showTimePickerDialog(firstTimeFragment, R.id.showTimePicker1ActivityButton);
                break;
            case R.id.showTimePicker2ActivityButton:
                showTimePickerDialog(secondTimeFragment, R.id.showTimePicker2ActivityButton);
                break;
            case R.id.computeDiffButton:
                computeDifference();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle firstDateBundle = firstDateFragment.getArguments();
        Bundle firstTimeBundle = firstTimeFragment.getArguments();
        Bundle secondDateBundle = secondDateFragment.getArguments();
        Bundle secondTimeBundle = secondTimeFragment.getArguments();
        outState.putInt("first_year", firstDateBundle.getInt("year"));
        outState.putInt("first_month", firstDateBundle.getInt("month"));
        outState.putInt("first_day", firstDateBundle.getInt("day"));
        outState.putInt("first_hour", firstTimeBundle.getInt("hour"));
        outState.putInt("first_minute", firstTimeBundle.getInt("minute"));
        outState.putInt("second_year", secondDateBundle.getInt("year"));
        outState.putInt("second_month", secondDateBundle.getInt("month"));
        outState.putInt("second_day", secondDateBundle.getInt("day"));
        outState.putInt("second_hour", secondTimeBundle.getInt("hour"));
        outState.putInt("second_minute", secondTimeBundle.getInt("minute"));
        outState.putString("date1String", showDatePicker1ActivityButton.getText().toString());
        outState.putString("date2String", showDatePicker2ActivityButton.getText().toString());
        outState.putString("time1String", showTimePicker1ActivityButton.getText().toString());
        outState.putString("time2String", showTimePicker2ActivityButton.getText().toString());
        outState.putString("diffTimeString", diffTime.toString("#ffff00", "#ffffff", true));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bundle firstDateBundle = new Bundle();
        Bundle firstTimeBundle = new Bundle();
        Bundle secondDateBundle = new Bundle();
        Bundle secondTimeBundle = new Bundle();
        firstDateBundle.putInt("year", savedInstanceState.getInt("first_year"));
        firstDateBundle.putInt("month", savedInstanceState.getInt("first_month"));
        firstDateBundle.putInt("day", savedInstanceState.getInt("first_day"));
        firstTimeBundle.putInt("hour", savedInstanceState.getInt("first_hour"));
        firstTimeBundle.putInt("minute", savedInstanceState.getInt("first_minute"));
        secondDateBundle.putInt("year", savedInstanceState.getInt("second_year"));
        secondDateBundle.putInt("month", savedInstanceState.getInt("second_month"));
        secondDateBundle.putInt("day", savedInstanceState.getInt("second_day"));
        secondTimeBundle.putInt("hour", savedInstanceState.getInt("second_hour"));
        secondTimeBundle.putInt("minute", savedInstanceState.getInt("second_minute"));
        firstDateFragment.setArguments(firstDateBundle);
        secondDateFragment.setArguments(secondDateBundle);
        firstTimeFragment.setArguments(firstTimeBundle);
        secondTimeFragment.setArguments(secondTimeBundle);
        showDatePicker1ActivityButton.setText(savedInstanceState.getString("date1String"));
        showDatePicker2ActivityButton.setText(savedInstanceState.getString("date2String"));
        showTimePicker1ActivityButton.setText(savedInstanceState.getString("time1String"));
        showTimePicker2ActivityButton.setText(savedInstanceState.getString("time2String"));
        differenceTextView.setText(Html.fromHtml(savedInstanceState.getString("diffTimeString")));
    }
}
