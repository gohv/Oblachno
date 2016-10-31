package xyz.georgihristov.oblachno.ui.oblachno.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by gohv on 08.09.16.
 */
public class Daily implements Parcelable {

    private long time;
    private String summary;
    private double temperatureMax;
    private String icon;
    private String timeZone;

    public Daily() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getTemperatureMax() {
        return (int) Math.round(temperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getIconId() {
        return ForeCast.getIconId(icon);
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date dateTime = new Date(time * 1000);
        return formatter.format(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //wraps the data
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(temperatureMax);
        dest.writeString(icon);
        dest.writeString(timeZone);

    }

    //unwraps the data - exactly the same order!!!!
    private Daily(Parcel in) {
        time = in.readLong();
        summary = in.readString();
        temperatureMax = in.readDouble();
        icon = in.readString();
        timeZone = in.readString();
    }

    public static final Creator<Daily> CREATOR
            = new Creator<Daily>() {
        public Daily createFromParcel(Parcel in) {
            return new Daily(in);
        }

        @Override
        public Daily[] newArray(int size) {
            return new Daily[size];
        }

    };
}
