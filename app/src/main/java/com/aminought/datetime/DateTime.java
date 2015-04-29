package com.aminought.datetime;

public class DateTime {
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    private int[] dayOfMonths = {31,28,31,30,31,30,31,31,30,31,30,31};

    public DateTime(int year, int month, int day, int hour, int minute) {
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public DateTime diff(DateTime x) {
        boolean offset = false;
        DateTime newDT = new DateTime(0,0,0,0,0);
        if(compare(this, x) == -1) {
            swap(this, x);
        }

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

    /**
     * Compare method
     * @param a
     * @param b
     * @return 0 if equal, 1 if a is less than b and -1 if a is greater than b
     */
    private int compare(DateTime a, DateTime b) {
        if(a.year > b.year) return 1;
        if(a.month > b.month) return 1;
        if(a.day > b.day) return 1;
        if(a.hour > b.hour) return 1;
        if(a.minute > b.minute) return 1;

        if(a.year < b.year) return -1;
        if(a.month < b.month) return -1;
        if(a.day < b.day) return -1;
        if(a.hour < b.hour) return -1;
        if(a.minute < b.minute) return -1;

        return 0;
    }

    private void swap(DateTime a, DateTime b) {
        DateTime tmp = new DateTime(b.year, b.month, b.day, b.hour, b.minute);
        b.year = a.year;
        b.month = a.month;
        b.day = a.day;
        b.hour = a.hour;
        b.minute = a.minute;
        a.year = tmp.year;
        a.month = tmp.month;
        a.day = tmp.day;
        a.hour = tmp.hour;
        a.minute = tmp.minute;
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
