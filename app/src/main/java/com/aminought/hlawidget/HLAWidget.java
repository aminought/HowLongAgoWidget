package com.aminought.hlawidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.widget.RemoteViews;

import com.aminought.datetime.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HLAWidget extends AppWidgetProvider {

    final String UPDATE_ALL_WIDGETS = "update_all_widgets";
    final String DELETE_ALL_WIDGETS = "delete_all_widgets";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] ids) {
        updateAll(context, appWidgetManager, ids);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            HLAWidgetConfigureActivity.database.deleteEvent(context, id);
        }
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Load data from preferences
        Event event = HLAWidgetConfigureActivity.database.loadEvent(context, appWidgetId);
        String full_text = event.event + "<br>";
        String dt = event.datetime;
        String image = event.image;

        Calendar event_calendar = new GregorianCalendar();
        Calendar now_calendar = new GregorianCalendar();

        Date event_date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            event_date = format.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date now_date = new Date();

        // Create calendars with event time and current time
        event_calendar.setTime(event_date);
        now_calendar.setTime(now_date);

        DateTime dtA = new DateTime(event_calendar.get(Calendar.YEAR),
                                    event_calendar.get(Calendar.MONTH),
                                    event_calendar.get(Calendar.DAY_OF_MONTH),
                                    event_calendar.get(Calendar.HOUR_OF_DAY),
                                    event_calendar.get(Calendar.MINUTE));
        DateTime dtB = new DateTime(now_calendar.get(Calendar.YEAR),
                                    now_calendar.get(Calendar.MONTH),
                                    now_calendar.get(Calendar.DAY_OF_MONTH),
                                    now_calendar.get(Calendar.HOUR_OF_DAY),
                                    now_calendar.get(Calendar.MINUTE));
        // Compute the difference
        DateTime dtNew = dtB.diff(dtA);

        full_text += dtNew.toString("#ffff00", "#ffffff", false);

        // Construct the RemoteViews object
        // Write difference in text view and set image in image view
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.hlawidget);
        views.setTextViewText(R.id.mainTextView, Html.fromHtml(full_text));
        if(!image.equals("")) {
            views.setImageViewBitmap(R.id.imageView, BitmapFactory.decodeFile(image));
        } else {
            views.setImageViewResource(R.id.imageView, R.mipmap.icon);
        }

        // Create intent for cofigure activity by pressing on widget
        Intent configIntent = new Intent(context, HLAWidgetConfigureActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId,
                                                                configIntent, 0);
        views.setOnClickPendingIntent(R.id.mainLayout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // Send broadcast message to update all widgets
        Intent intent = new Intent(context, com.aminought.hlawidget.HLAWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pIntent);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(),
                                                        getClass().getName());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
        updateAll(context, appWidgetManager, ids);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, HLAWidget.class);
        intent.setAction(DELETE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        ComponentName thisAppWidget = new ComponentName(
                context.getPackageName(), getClass().getName());
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);

        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            updateAll(context, appWidgetManager, ids);
        } else if(intent.getAction().equalsIgnoreCase(DELETE_ALL_WIDGETS)) {
            deleteAll(context, ids);
        }
    }

    private void updateAll(Context context, AppWidgetManager appWidgetManager, int[] ids) {
        for (int appWidgetID : ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);
        }
    }

    private void deleteAll(Context context, int[] ids) {
        Database db = new Database();
        for(int id : ids) {
            db.deleteEvent(context, id);
        }
    }
}


