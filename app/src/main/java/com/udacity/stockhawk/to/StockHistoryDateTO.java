package com.udacity.stockhawk.to;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by David on 12/20/16.
 */
public class StockHistoryDateTO implements Serializable {

    private static final long serialVersionUID = 6692632912055759354L;

    @SerializedName("year")
    private Integer year;
    @SerializedName("month")
    private Integer month;
    @SerializedName("dayOfMonth")
    private Integer dayOfMonth;
    @SerializedName("hourOfDay")
    private Integer hourOfDay;
    @SerializedName("minute")
    private Integer minute;
    @SerializedName("second")
    private Integer second;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(Integer hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }
}
