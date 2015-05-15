package com.aminought.datetime;

import java.util.Calendar;
import java.util.Locale;

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

    public DateTime(Calendar date) {
        this.year = date.get(Calendar.YEAR);
        this.month = date.get(Calendar.MONTH);
        this.day = date.get(Calendar.DAY_OF_MONTH);
        this.hour = date.get(Calendar.HOUR_OF_DAY);
        this.minute = date.get(Calendar.MINUTE);
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
        if(a.year < b.year) return -1;
        if(a.month > b.month) return 1;
        if(a.month < b.month) return -1;
        if(a.day > b.day) return 1;
        if(a.day < b.day) return -1;
        if(a.hour > b.hour) return 1;
        if(a.hour < b.hour) return -1;
        if(a.minute > b.minute) return 1;
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

    private String getRussianWordDependsOfNumber(int number, String one, String two_four, String other) {
        String s = String.valueOf(number);
        String lastLetter = s.substring(s.length()-1, s.length());
        String retValue = "";
        if (lastLetter.equals("1") && number != 11) retValue = one;
        else if ((lastLetter.equals("2") || lastLetter.equals("3") || lastLetter.equals("4"))
                && (number < 12 || number > 14)) {
            retValue += two_four;
        }
        else retValue += other;
        return retValue;
    }

    public String toString(String textColor1, String textColor2, boolean isLB) {
        String full_text = "<small>";
        String color1 = "<font color='" + textColor1 +"'>";
        String color2 = "<font color='" + textColor2 +"'>";
        String font = "</font>";
        if(Locale.getDefault().toString().equals("ru_RU")) {
            if (this.year > 0) {
                full_text += color1 + this.year + font + color2;
                full_text += getRussianWordDependsOfNumber(this.year, " год ", " года ", " лет ");
                full_text += font + (isLB ? "<br>" : "");
            }
            if (this.month > 0) {
                full_text += color1 + this.month + font + color2;
                full_text += getRussianWordDependsOfNumber(this.month, " месяц ", " месяца ", " месяцев ");
                full_text += font + (isLB ? "<br>" : "");
            }
            if (this.day > 0) {
                full_text += color1 + this.day + font + color2;
                full_text += getRussianWordDependsOfNumber(this.day, " день ", " дня ", " дней ");
                full_text += font + (isLB ? "<br>" : "");
            }
            if (this.hour > 0) {
                full_text += color1 + this.hour + font + color2;
                full_text += getRussianWordDependsOfNumber(this.hour, " час ", " часа ", " часов ");
                full_text += font + (isLB ? "<br>" : "");
            }
            if (this.minute > 0) {
                full_text += color1 + this.minute + font + color2;
                full_text += getRussianWordDependsOfNumber(this.minute, " минута ", " минуты ", " минут ");
                full_text += font + (isLB ? "<br>" : "");
            }
        } else {
            if (this.year > 0) full_text += color1 + this.year + font + color2 +
                    (this.year == 1 ? " year " : " years ") + font + (isLB ? "<br>" : "");
            if (this.month > 0) full_text += color1 + this.month + font + color2 +
                    (this.month == 1 ? " month " : " months ") + font + (isLB ? "<br>" : "");
            if (this.day > 0) full_text += color1 + this.day + font + color2 +
                    (this.day == 1 ? " day " : " days ") + font + (isLB ? "<br>" : "");
            if (this.hour > 0) full_text += color1 + this.hour + font + color2 +
                    (this.hour == 1 ? " hour " : " hours ") + font + (isLB ? "<br>" : "");
            if (this.minute > 0) full_text += color1 + this.minute + font + color2 +
                    (this.minute == 1 ? " minute " : " minutes ") + font + (isLB ? "<br>" : "");
        }
        full_text += "</small>";
        return full_text;
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
