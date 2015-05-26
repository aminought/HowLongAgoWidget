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
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aminought.analytics.GoogleAnalyticsApp;
import com.aminought.datetime.DatePickerFragment;
import com.aminought.datetime.DateTime;
import com.aminought.datetime.TimePickerFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;

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
    private CheckBox addImageCheckBox;
    LinearLayout showImagePickerLinearLayout;
    private Bundle previewBundle;

    private final int PICK_IMAGE = 1;

    public HLAWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.hlawidget_configure);

        ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);

        dateFragment = new DatePickerFragment();
        timeFragment = new TimePickerFragment();
        EditText eventEditText = (EditText) findViewById(R.id.eventEditText);
        Button addWidgetButton = (Button) findViewById(R.id.addWidgetButton);
        Button chooseImageButton = (Button) findViewById(R.id.chooseImageButton);
        Button resetImageButton = (Button) findViewById(R.id.resetImageButton);
        addImageCheckBox = (CheckBox) findViewById(R.id.addImage);
        configImageView = (ImageView) findViewById(R.id.configImageView);
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Load data from preferences
        event = database.loadEvent(this, mAppWidgetId);
        previewBundle = new Bundle();
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

        addWidgetButton.setOnClickListener(this);
        chooseImageButton.setOnClickListener(this);
        resetImageButton.setOnClickListener(this);
        addImageCheckBox.setOnClickListener(this);

        showImagePickerLinearLayout = (LinearLayout)
                findViewById(R.id.showImagePickerLinearLayout);
        addImageCheckBox.setChecked(event.isAddImage);
        showImagePicker();

        // Set image
        configImageView.setOnClickListener(this);
        previewBundle.putString("image", event.image);
        updateImage(event.image);

        // Apply custom font for title
        Typeface font = Typeface.createFromAsset(getAssets(), "BuxtonSketch.ttf");
        titleTextView.setTypeface(font);

        // Write date and time into text views
        showDatePickerButton = (TextView) findViewById(R.id.showDatePickerButton);
        Calendar dateCal = new GregorianCalendar(dateArgs.getInt("year"),
                dateArgs.getInt("month"), dateArgs.getInt("day"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        showDatePickerButton.setText(dateFormat.format(dateCal.getTime()));
        showDatePickerButton.setOnClickListener(this);
        showDatePickerButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePreview();
            }
            @Override
            public void afterTextChanged(Editable s) {  }
        });

        showTimePickerButton = (TextView) findViewById(R.id.showTimePickerButton);
        Calendar timeCal = new GregorianCalendar(0, 0, 0, timeArgs.getInt("hour"),
                timeArgs.getInt("minute"));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        showTimePickerButton.setText(timeFormat.format(timeCal.getTime()));
        showTimePickerButton.setOnClickListener(this);
        showTimePickerButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePreview();
            }
            @Override
            public void afterTextChanged(Editable s) {  }
        });

        updatePreview();

        eventEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePreview();
            }
            @Override
            public void afterTextChanged(Editable s) {  }
        });

        // Add ads
        AdView adView = (AdView) findViewById(R.id.ConfigureAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    private void updateImage(String image) {
        if(!image.equals("")) {
            Bitmap bitmap = com.aminought.bitmap.Bitmap.decodeSampledBitmapFromResource(image, 100, 100);
            configImageView.setImageBitmap(bitmap);
        } else {
            configImageView.setImageResource(R.drawable.icon_100);
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

    private String getDateTimeString() {
        Bundle dateArgs = dateFragment.getArguments();
        Bundle timeArgs = timeFragment.getArguments();
        return  dateArgs.getInt("year") + "-" +
                (dateArgs.getInt("month")+1) + "-" +
                dateArgs.getInt("day") + " " +
                timeArgs.getInt("hour") + ":" +
                timeArgs.getInt("minute");
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addWidgetButton:
                final Context context = com.aminought.hlawidget.HLAWidgetConfigureActivity.this;
                // When the button is clicked, store the string locally
                EditText eventEditText = (EditText) findViewById(R.id.eventEditText);

                // Save text and date into event object
                event.text = eventEditText.getText().toString();
                event.datetime = getDateTimeString();
                event.isAddImage = addImageCheckBox.isChecked();
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
                updatePreview();
                break;
            case R.id.addImage:
                showImagePicker();
                updatePreview();
                break;
        }
    }

    private void showImagePicker() {
        if(addImageCheckBox.isChecked()) {
            showImagePickerLinearLayout.setVisibility(View.VISIBLE);
        } else {
            showImagePickerLinearLayout.setVisibility(View.GONE);
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
        previewBundle.putString("image", "");
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
                    previewBundle.putString("image", imageDecodableString);
                    updatePreview();
                    break;
            }
        }
    }

    private void updatePreview() {
        String image = previewBundle.getString("image", "");
        ImageView imageView = (ImageView) findViewById(R.id.imageViewPreview);
        if(addImageCheckBox.isChecked()) {
            if (!image.equals("")) {
                android.graphics.Bitmap bitmap = com.aminought.bitmap.Bitmap.
                        decodeSampledBitmapFromResource(image, 100, 100);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.mipmap.icon);
            }
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        EditText eventEditText = (EditText) findViewById(R.id.eventEditText);
        String fullText = eventEditText.getText() + "<br>";
        String datetime = getDateTimeString();
        DateTime dtNew = DateTime.computeTimeToNow(datetime);
        fullText += dtNew.toString("#ffff00", "#ffffff", false);
        TextView mainTextViewPreview = (TextView) findViewById(R.id.mainTextViewPreview);
        mainTextViewPreview.setText(Html.fromHtml(fullText));
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
        outState.putBoolean("addImageCheckBox", addImageCheckBox.isChecked());
        outState.putString("image", previewBundle.getString("image"));
        outState.putString("event.image", event.image);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
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
        addImageCheckBox.setChecked(savedInstanceState.getBoolean("addImageCheckBox"));
        previewBundle.putString("image", savedInstanceState.getString("image"));
        event.image = savedInstanceState.getString("event.image");
        updateImage(event.image);
        showImagePicker();
        updatePreview();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
