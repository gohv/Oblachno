package xyz.georgihristov.oblachno.ui.oblachno.model;

import oblachno.xyz.georgihristov.ui.R;

/**
 * Created by gohv on 08.09.16.
 */
public class ForeCast {

    private CurrentWeather currentWeather;
    private Hour[] hourlyForecast;
    private Daily[] dailyForecast;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public Hour[] getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public Daily[] getDailyForecast() {
        return dailyForecast;
    }

    public void setDailyForecast(Daily[] dailyForecast) {
        this.dailyForecast = dailyForecast;
    }



     public static int getIconId(String icon){
         // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
         int iconId = R.drawable.clear_day;

         if (icon.equals("clear-day")) {
             iconId = R.drawable.clear_day;
         }
         else if (icon.equals("clear-night")) {
             iconId = R.drawable.clear_night;
         }
         else if (icon.equals("rain")) {
             iconId = R.drawable.rain;
         }
         else if (icon.equals("snow")) {
             iconId = R.drawable.snow;
         }
         else if (icon.equals("sleet")) {
             iconId = R.drawable.sleet;
         }
         else if (icon.equals("wind")) {
             iconId = R.drawable.wind;
         }
         else if (icon.equals("fog")) {
             iconId = R.drawable.fog;
         }
         else if (icon.equals("cloudy")) {
             iconId = R.drawable.cloudy;
         }
         else if (icon.equals("partly-cloudy-day")) {
             iconId = R.drawable.partly_cloudy;
         }
         else if (icon.equals("partly-cloudy-night")) {
             iconId = R.drawable.cloudy_night;
         }

         return iconId;

     }

}
