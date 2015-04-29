package com.aminought.hlawidget;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    public String event;
    public String datetime;

    public Event() {
        this.event = "";
        Date now_date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.datetime = format.format(now_date);
    }

    public Event(String event, String datetime) {
        this.event = event;
        this.datetime = datetime;
    }
}
