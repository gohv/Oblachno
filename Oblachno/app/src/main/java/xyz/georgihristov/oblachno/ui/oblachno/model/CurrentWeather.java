package xyz.georgihristov.oblachno.ui.oblachno.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import oblachno.xyz.georgihristov.ui.R;

/**
 * Created by gohv on 08.09.16.
 */
public class CurrentWeather {

    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double rainChance;
    private String summary;
    private String timeZone;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getIcon() {
        return icon;
    }

    public int getIconId() {
    return ForeCast.getIconId(icon);
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getTemperature() {
        return (int)Math.round(temperature);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getRainChance() {
        return rainChance;
    }

    public void setRainChance(double rainChance) {
        this.rainChance = rainChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
