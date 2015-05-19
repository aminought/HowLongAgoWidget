package com.aminought.hlawidget;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    public String text;
    public String datetime;
    public String image;
    boolean isAddImage;

    public Event() {
        this.text = "";
        Date now_date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.datetime = format.format(now_date);
        this.isAddImage = false;
        this.image = "";
    }

    public Event(String text, String datetime, String image, boolean isAddImage) {
        this.text = text;
        this.datetime = datetime;
        this.image = image;
        this.isAddImage = isAddImage;
    }
}
