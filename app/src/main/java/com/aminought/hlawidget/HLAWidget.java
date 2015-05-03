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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] ids) {
        updateAll(context, appWidgetManager, ids);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            HLAWidgetConfigureActivity.database.delete(context, id);
        }
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Event event = HLAWidgetConfigureActivity.database.load(context, appWidgetId);
        String full_text = event.event + "<br><small>";
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
        DateTime dtNew = dtB.diff(dtA);

        String color = "<font color='#FFFF00'>";
        String font = "</font>";

        if(dtNew.getYear() > 0) full_text += color + dtNew.getYear() +
                font + (dtNew.getYear()==1 ? " year " : " years ");
        if(dtNew.getMonth() > 0) full_text += color + dtNew.getMonth() +
                font + (dtNew.getMonth()==1 ? " month " : " months ");
        if(dtNew.getDay() > 0) full_text += color + dtNew.getDay() +
                font + (dtNew.getDay()==1 ? " day " : " days ");
        if(dtNew.getHour() > 0) full_text += color + dtNew.getHour()
                + font + (dtNew.getHour()==1 ? " hour " : " hours ");
        if(dtNew.getMinute() > 0) full_text += color + dtNew.getMinute()
                + font + (dtNew.getMinute()==1 ? " minute " : " minutes ");
        full_text += "</small>";

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.hlawidget);
        views.setTextViewText(R.id.mainTextView, Html.fromHtml(full_text));
        views.setImageViewBitmap(R.id.imageView, BitmapFactory.decodeFile(image));

        Intent configIntent = new Intent(context, HLAWidgetConfigureActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId,
                                                                configIntent, 0);
        views.setOnClickPendingIntent(R.id.imageView, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
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
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            updateAll(context, appWidgetManager, ids);
        }
    }

    private void updateAll(Context context, AppWidgetManager appWidgetManager, int[] ids) {
        for (int appWidgetID : ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);
        }
    }
}


