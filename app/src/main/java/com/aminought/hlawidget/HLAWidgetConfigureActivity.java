package com.aminought.hlawidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import com.aminought.datetime.DateTimeCurrentState;
import com.aminought.datetime.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HLAWidgetConfigureActivity extends FragmentActivity implements View.OnClickListener {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public Event event = new Event();
    public static Database database = new Database();
    private DialogFragment newDateFragment = new DatePickerFragment();
    private DialogFragment newTimeFragment = new TimePickerFragment();
    private ImageView configImageView;

    private final int PICK_IMAGE = 1;

    public HLAWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.hlawidget_configure);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        EditText eventEditText = (EditText) findViewById(R.id.eventEditText);

        event = database.load(this, mAppWidgetId);
        eventEditText.setText(event.event);

        Calendar calendar = new GregorianCalendar();

        Date event_date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            event_date = format.parse(event.datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(event_date);

        DateTimeCurrentState.year = calendar.get(Calendar.YEAR);
        DateTimeCurrentState.month = calendar.get(Calendar.MONTH);
        DateTimeCurrentState.day = calendar.get(Calendar.DAY_OF_MONTH);
        DateTimeCurrentState.hour = calendar.get(Calendar.HOUR_OF_DAY);
        DateTimeCurrentState.minute = calendar.get(Calendar.MINUTE);

        Bundle dateArgs = new Bundle();
        dateArgs.putInt("year", DateTimeCurrentState.year);
        dateArgs.putInt("month", DateTimeCurrentState.month);
        dateArgs.putInt("day", DateTimeCurrentState.day);

        newDateFragment.setArguments(dateArgs);

        Bundle timeArgs = new Bundle();
        timeArgs.putInt("hour", DateTimeCurrentState.hour);
        timeArgs.putInt("minute", DateTimeCurrentState.minute);

        newTimeFragment.setArguments(timeArgs);

        Button addWidgetButton = (Button) findViewById(R.id.addWidgetButton);
        addWidgetButton.setOnClickListener(this);

        Button chooseImageButton = (Button) findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(this);

        Button resetImageButton = (Button) findViewById(R.id.resetImageButton);
        resetImageButton.setOnClickListener(this);

        configImageView = (ImageView) findViewById(R.id.configImageView);
        if(!event.image.equals("")) {
            configImageView.setImageBitmap(BitmapFactory.decodeFile(event.image));
        } else {
            configImageView.setImageResource(R.drawable.icon_100);
        }

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        Typeface font = Typeface.createFromAsset(getAssets(), "BuxtonSketch.ttf");
        titleTextView.setTypeface(font);

        TextView showDatePickerButton = (TextView) findViewById(R.id.showDatePickerButton);
        Calendar dateCal = new GregorianCalendar(DateTimeCurrentState.year,
                                                DateTimeCurrentState.month,
                                                DateTimeCurrentState.day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        showDatePickerButton.setText(dateFormat.format(dateCal.getTime()));
        showDatePickerButton.setOnClickListener(this);

        TextView showTimePickerButton = (TextView) findViewById(R.id.showTimePickerButton);
        Calendar timeCal = new GregorianCalendar(0, 0, 0, DateTimeCurrentState.hour,
                                                 DateTimeCurrentState.minute);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        showTimePickerButton.setText(timeFormat.format(timeCal.getTime()));
        showTimePickerButton.setOnClickListener(this);

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }

    public void showDatePickerDialog() {
        Bundle dateArgs = new Bundle();
        dateArgs.putInt("year", DateTimeCurrentState.year);
        dateArgs.putInt("month", DateTimeCurrentState.month);
        dateArgs.putInt("day", DateTimeCurrentState.day);
        dateArgs.putInt("view_id", R.id.showDatePickerButton);
        newDateFragment.setArguments(dateArgs);
        newDateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog() {
        Bundle timeArgs = new Bundle();
        timeArgs.putInt("hour", DateTimeCurrentState.hour);
        timeArgs.putInt("minute", DateTimeCurrentState.minute);
        timeArgs.putInt("view_id", R.id.showTimePickerButton);
        newTimeFragment.setArguments(timeArgs);
        newTimeFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addWidgetButton:
                final Context context = com.aminought.hlawidget.HLAWidgetConfigureActivity.this;
                // When the button is clicked, store the string locally
                EditText eventEditText = (EditText) findViewById(R.id.eventEditText);

                int month = DateTimeCurrentState.month + 1;
//                event = new Event();
                event.event = eventEditText.getText().toString();
                event.datetime = DateTimeCurrentState.year + "-" +
                                month + "-" +
                                DateTimeCurrentState.day + " " +
                                DateTimeCurrentState.hour + ":" +
                                DateTimeCurrentState.minute;
                database.save(context, event, mAppWidgetId);

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
                    configImageView.setImageURI(image);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(image, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imageDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    event.image = imageDecodableString;
                    break;
            }
        }
    }
}
