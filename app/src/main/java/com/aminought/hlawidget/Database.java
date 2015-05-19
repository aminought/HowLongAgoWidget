package com.aminought.hlawidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Database {
    private SharedPreferences sPref;
    private final String FILENAME = "hla_pref";

    public void saveEvent(Context context, Event event, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("event" + id, event.text);
        ed.putString("datetime" + id, event.datetime);
        ed.putString("image" + id, event.image);
        ed.putBoolean("isAddImage" + id, event.isAddImage);
        ed.apply();
    }

    public Event loadEvent(Context context, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        return new Event(sPref.getString("event" + id, ""),
                         sPref.getString("datetime" + id, ""),
                         sPref.getString("image" + id, ""),
                         sPref.getBoolean("isAddImage" + id, false));
    }

    public void deleteEvent(Context context, int id) {
        sPref = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.remove("event" + id);
        ed.remove("datetime" + id);
        ed.remove("image" + id);
        ed.remove("isAddImage" + id);
        ed.apply();
    }
}
