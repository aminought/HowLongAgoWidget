package com.aminought.hlawidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aminought.datetime.DatePickerFragment;
import com.aminought.datetime.TimePickerFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HLAWidgetConfigureActivity extends FragmentActivity implements View.OnClickListener {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public Event event = new Event();
    public static Database database = new Database();
    private DialogFragment dateFragment;
    private DialogFragment timeFragment;
    private ImageView configImageView;
    private TextView showDatePickerButton;
    private TextView showTimePickerButton;

    private final int PICK_IMAGE = 1;

    public HLAWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.hlawidget_configure);

        dateFragment = new DatePickerFragment();
        timeFragment = new TimePickerFragment();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        EditText eventEditText = (EditText) findViewById(R.id.eventEditText);

        // Load data from preferences
        event = database.loadEvent(this, mAppWidgetId);
        eventEditText.setText(event.text);

        // Create calendar with event date
        // If event date is empty then calendar has current time
        Calendar calendar = new GregorianCalendar();
        Date event_date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            event_date = format.parse(event.datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(event_date);

        // Send event date into DateFragment
        Bundle dateArgs = new Bundle();
        dateArgs.putInt("year", calendar.get(Calendar.YEAR));
        dateArgs.putInt("month", calendar.get(Calendar.MONTH));
        dateArgs.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
        dateFragment.setArguments(dateArgs);

        // Send event time into TimeFragment
        Bundle timeArgs = new Bundle();
        timeArgs.putInt("hour", calendar.get(Calendar.HOUR_OF_DAY));
        timeArgs.putInt("minute", calendar.get(Calendar.MINUTE));
        timeFragment.setArguments(timeArgs);

        Button addWidgetButton = (Button) findViewById(R.id.addWidgetButton);
        addWidgetButton.setOnClickListener(this);

        Button chooseImageButton = (Button) findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(this);

        Button resetImageButton = (Button) findViewById(R.id.resetImageButton);
        resetImageButton.setOnClickListener(this);

        // Set image
        configImageView = (ImageView) findViewById(R.id.configImageView);
        configImageView.setOnClickListener(this);
        if(!event.image.equals("")) {
            Bitmap bitmap = com.aminought.bitmap.Bitmap.decodeSampledBitmapFromResource(event.image, 100, 100);
            configImageView.setImageBitmap(bitmap);
        } else {
            configImageView.setImageResource(R.drawable.icon_100);
        }

        // Apply custom font for title
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        Typeface font = Typeface.createFromAsset(getAssets(), "BuxtonSketch.ttf");
        titleTextView.setTypeface(font);

        // Write date and time into text views
        showDatePickerButton = (TextView) findViewById(R.id.showDatePickerButton);
        Calendar dateCal = new GregorianCalendar(dateArgs.getInt("year"),
                dateArgs.getInt("month"), dateArgs.getInt("day"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        showDatePickerButton.setText(dateFormat.format(dateCal.getTime()));
        showDatePickerButton.setOnClickListener(this);

        showTimePickerButton = (TextView) findViewById(R.id.showTimePickerButton);
        Calendar timeCal = new GregorianCalendar(0, 0, 0, timeArgs.getInt("hour"),
                timeArgs.getInt("minute"));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        showTimePickerButton.setText(timeFormat.format(timeCal.getTime()));
        showTimePickerButton.setOnClickListener(this);

        // Add ads
        AdView adView = (AdView) findViewById(R.id.ConfigureAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    public void showDatePickerDialog() {
        Bundle dateArgs = dateFragment.getArguments();
        dateArgs.putInt("view_id", R.id.showDatePickerButton);
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog() {
        Bundle timeArgs = timeFragment.getArguments();
        timeArgs.putInt("view_id", R.id.showTimePickerButton);
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addWidgetButton:
                final Context context = com.aminought.hlawidget.HLAWidgetConfigureActivity.this;
                // When the button is clicked, store the string locally
                EditText eventEditText = (EditText) findViewById(R.id.eventEditText);

                // Save text and date into event object
                Bundle dateArgs = dateFragment.getArguments();
                Bundle timeArgs = timeFragment.getArguments();
                event.text = eventEditText.getText().toString();
                event.datetime = dateArgs.getInt("year") + "-" +
                        (dateArgs.getInt("month")+1) + "-" +
                        dateArgs.getInt("day") + " " +
                        timeArgs.getInt("hour") + ":" +
                        timeArgs.getInt("minute");
                database.saveEvent(context, event, mAppWidgetId);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                HLAWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
                break;
            case R.id.showDatePickerButton:
                showDatePickerDialog();
                break;
            case R.id.showTimePickerButton:
                showTimePickerDialog();
                break;
            case R.id.chooseImageButton:
            case R.id.configImageView:
                chooseImage();
                break;
            case R.id.resetImageButton:
                resetImage();
                break;
        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                                   MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Выбрать изображение"), PICK_IMAGE);
    }

    private void resetImage() {
        configImageView.setImageResource(R.mipmap.icon);
        event.image = "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case PICK_IMAGE:
                    Uri image = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(image, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imageDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    event.image = imageDecodableString;
                    Bitmap bitmap = com.aminought.bitmap.Bitmap.decodeSampledBitmapFromResource(event.image, 100, 100);
                    configImageView.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle dateArgs = dateFragment.getArguments();
        Bundle timeArgs = timeFragment.getArguments();
        outState.putInt("year", dateArgs.getInt("year"));
        outState.putInt("month", dateArgs.getInt("month"));
        outState.putInt("day", dateArgs.getInt("day"));
        outState.putInt("hour", timeArgs.getInt("hour"));
        outState.putInt("minute", timeArgs.getInt("minute"));
        outState.putString("dateString", showDatePickerButton.getText().toString());
        outState.putString("timeString", showTimePickerButton.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bundle dateArgs = new Bundle();
        Bundle timeArgs = new Bundle();
        dateArgs.putInt("year", savedInstanceState.getInt("year"));
        dateArgs.putInt("month", savedInstanceState.getInt("month"));
        dateArgs.putInt("day", savedInstanceState.getInt("day"));
        timeArgs.putInt("hour", savedInstanceState.getInt("hour"));
        timeArgs.putInt("minute", savedInstanceState.getInt("minute"));
        dateFragment.setArguments(dateArgs);
        timeFragment.setArguments(timeArgs);
        showDatePickerButton.setText(savedInstanceState.getString("dateString"));
        showTimePickerButton.setText(savedInstanceState.getString("timeString"));
    }
}
