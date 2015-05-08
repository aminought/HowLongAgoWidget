package com.aminought.hlawidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.aminought.datetime.DateTime;

public class Database {
    private SharedPreferences sPref;
    private final String FILENAME = "hla_pref";

    public void saveEvent(Context context, Event event, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("event" + id, event.event);
        ed.putString("datetime" + id, event.datetime);
        ed.putString("image" + id, event.image);
        ed.apply();
    }

    public void saveDateTime(Context context, DateTime dt, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putInt("minute" + id, dt.getMinute());
        ed.putInt("hour" + id, dt.getHour());
        ed.putInt("day" + id, dt.getDay());
        ed.putInt("month" + id, dt.getMonth());
        ed.putInt("year" + id, dt.getYear());
        ed.apply();
    }

    public Event loadEvent(Context context, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return new Event(sPref.getString("event" + id, ""),
                         sPref.getString("datetime" + id, ""),
                         sPref.getString("image" + id, ""));
    }

    public DateTime loadDateTime(Context context, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return new DateTime(sPref.getInt("minute" + id, 0),
                sPref.getInt("hour" + id, 0),
                sPref.getInt("day" + id, 0),
                sPref.getInt("month" + id, 0),
                sPref.getInt("year" + id, 0));
    }

    public void deleteEvent(Context context, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.remove("event" + id);
        ed.remove("datetime" + id);
        ed.remove("image" + id);
        ed.apply();
    }

    public void deleteDateTime(Context context, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.remove("minute" + id);
        ed.remove("hour" + id);
        ed.remove("day" + id);
        ed.remove("month" + id);
        ed.remove("year" + id);
        ed.apply();
    }
}
