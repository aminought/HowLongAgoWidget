package com.aminought.datetime;

public class DateTime {
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    private int[] dayOfMonths = {31,28,31,30,31,30,31,31,30,31,30,31};

    public DateTime(int minute, int hour, int day, int month, int year) {
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public DateTime diff(DateTime x) {
        boolean offset = false;
        DateTime newDT = new DateTime(0,0,0,0,0);

        int minute = this.minute - x.minute;
        if(minute < 0) {
            minute += 60;
            offset = true;
        }
        newDT.minute = minute;

        int hour = this.hour - x.hour;
        if(offset) hour--;
        if(hour < 0) {
            hour += 24;
            offset = true;
        } else {
            offset = false;
        }
        newDT.hour = hour;

        int day = this.day - x.day;
        if(offset) day--;
        if(day < 0) {
            day += dayOfMonths[x.month];
            offset = true;
        } else {
            offset = false;
        }
        newDT.day = day;

        int month = this.month - x.month;
        if(offset) month--;
        if(month < 0) {
            month += 12;
            offset = true;
        } else {
            offset = false;
        }
        newDT.month = month;

        int year = this.year - x.year;
        if(offset) year--;
        newDT.year = year;

        return newDT;
    }

    public int getDay() {
        return day;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
