package xyz.georgihristov.oblachno.ui.oblachno.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.OnClick;
import xyz.georgihristov.oblachno.ui.oblachno.dialog.AlertDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.georgihristov.oblachno.ui.oblachno.model.CurrentWeather;
import xyz.georgihristov.oblachno.ui.oblachno.model.Daily;
import xyz.georgihristov.oblachno.ui.oblachno.model.ForeCast;
import xyz.georgihristov.oblachno.ui.oblachno.model.Hour;
import oblachno.xyz.georgihristov.ui.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    private String timezone;
    private Location gpsLocation;
    private GoogleApiClient googleApiClient;
    double latitude;
    double longitude;

    //butterknife : Bind
    @BindView(R.id.temperatureLabel)
    TextView temperatureLabel;
    @BindView(R.id.timeLabel)
    TextView timeLabel;
    @BindView(R.id.humidityValue)
    TextView humidityLabel;
    @BindView(R.id.chanceOfRainValue)
    TextView chanceOfRainValue;
    @BindView(R.id.summaryLabel)
    TextView summaryLabel;
    @BindView(R.id.iconImageView)
    ImageView iconImageView;
    @BindView(R.id.refreshImageView)
    ImageView refreshImageView;
    @BindView(R.id.locationLabel)
    TextView locationLabel;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ForeCast foreCast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.INVISIBLE);
        buildGoogleApiClient();

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getForecast(latitude, longitude);

            }
        });
    }

    private void getForecast(double latitude, double longtitude) {
        //setting the keys:

        String apiKey = "32cb5a1fa23192dd20e21926034897f4";
        String forecastURI = "https://api.forecast.io/forecast/" +
                apiKey + "/" + latitude + "," + longtitude +"?units=si";

        //check if device has network
        if (isConnected()) {
            toggleRefresh();

            //setting the Http Client:
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURI)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();

                        if (response.isSuccessful()) {
                            foreCast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateWeather();
                                }
                            });


                        } else {
                            alertAboutError();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                      e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Please check your Device Internet State!", Toast.LENGTH_LONG).show();

        }
    }

    private void toggleRefresh() {
        if(progressBar.getVisibility() == View.INVISIBLE){
        progressBar.setVisibility(View.VISIBLE);
        refreshImageView.setVisibility(View.INVISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void updateWeather() {

        CurrentWeather currentWeather = foreCast.getCurrentWeather();
        timezone = currentWeather.getTimeZone();
        temperatureLabel.setText(currentWeather.getTemperature() + "");
        timeLabel.setText("At " + currentWeather.getFormattedTime() + " it will be  " );
        Drawable drawable = getResources().getDrawable(currentWeather.getIconId());
        iconImageView.setImageDrawable(drawable);
        humidityLabel.setText(currentWeather.getHumidity() + "");
        chanceOfRainValue.setText(currentWeather.getRainChance() + "");
        summaryLabel.setText(currentWeather.getSummary());
        locationLabel.setText(currentWeather.getTimeZone());

    }
    private ForeCast parseForecastDetails(String jsonData) throws JSONException {
        ForeCast foreCast = new ForeCast();

        foreCast.setCurrentWeather(getCurrentDetails(jsonData));
        foreCast.setHourlyForecast(getHourlyForecast(jsonData));
        foreCast.setDailyForecast(getDailyForecast(jsonData));


        return foreCast;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];
        for(int i = 0; i < data.length(); i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour =  new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimeZone(timeZone);
            hours[i] = hour;
        }
        return hours;
    }
    private Daily[] getDailyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        JSONObject dailyWeatherData = forecast.getJSONObject("daily");
        JSONArray data = dailyWeatherData.getJSONArray("data");

        Daily[] days = new Daily[data.length()];
        for(int i = 0; i < data.length(); i++){
            JSONObject jsonDaily = data.getJSONObject(i);
            Daily daily = new Daily();
            daily.setSummary(jsonDaily.getString("summary"));
            daily.setTemperatureMax(jsonDaily.getDouble("temperatureMax"));
            daily.setIcon(jsonDaily.getString("icon"));
            daily.setTime(jsonDaily.getLong("time"));
            daily.setTimeZone(timeZone);
            days[i] = daily;
        }


        return days;
    }

    //set weather data bellow:
    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");

        JSONObject jsonWeatherData = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(jsonWeatherData.getDouble("humidity"));
        currentWeather.setTimeZone(timeZone);
        currentWeather.setTime(jsonWeatherData.getLong("time"));
        currentWeather.setIcon(jsonWeatherData.getString("icon"));
        currentWeather.setRainChance(jsonWeatherData.getDouble("precipProbability"));
        currentWeather.setSummary(jsonWeatherData.getString("summary"));
        currentWeather.setTemperature(jsonWeatherData.getDouble("temperature"));
        return currentWeather;
    }


    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "TAG");
    }

    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, foreCast.getDailyForecast());
        intent.putExtra("timezone", timezone);
        startActivity(intent);
    }


    @OnClick (R.id.hourlyButton)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, foreCast.getHourlyForecast());
        startActivity(intent);

    }
        // Location Support Below:

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        gpsLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(gpsLocation != null){
            latitude = gpsLocation.getLatitude();
            longitude = gpsLocation.getLongitude();
            getForecast(latitude, longitude);
        }else{
            Toast.makeText(this, "No GPS connection found, please turn on GPS. Setting to default location, Sofia/Bulgaria", Toast.LENGTH_SHORT).show();
            latitude = 42.7339;
            longitude = 25.4858;
            getForecast(latitude,longitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }
}
