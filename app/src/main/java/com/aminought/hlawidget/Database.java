package com.aminought.hlawidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;

public class Database {
    private SharedPreferences sPref;
    private final String FILENAME = "hla_pref";

    public void save(Context context, Event event, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("event" + id, event.event);
        ed.putString("datetime" + id, event.datetime);
        ed.putString("image" + id, event.image);
        ed.apply();
    }

    public Event load(Context context, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return new Event(sPref.getString("event" + id, ""),
                         sPref.getString("datetime" + id, ""),
                         sPref.getString("image" + id, ""));
    }

    public void delete(Context context, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.remove("event" + id);
        ed.remove("datetime" + id);
        ed.remove("image" + id);
        ed.apply();
    }
}
