package xyz.georgihristov.oblachno.ui.oblachno.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import oblachno.xyz.georgihristov.ui.R;
import xyz.georgihristov.oblachno.ui.oblachno.adapters.DayAdapter;
import xyz.georgihristov.oblachno.ui.oblachno.model.Daily;

public class DailyForecastActivity extends ListActivity {
    private Daily[] days;
    private TextView locationLabel;
    private Bundle intentExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        days = Arrays.copyOf(parcelables, parcelables.length, Daily[].class);

        DayAdapter adapter = new DayAdapter(this, days);
        setListAdapter(adapter);
        locationLabel = (TextView) findViewById(R.id.locationLabel);
        intentExtras = intent.getExtras();
        locationLabel.setText(String.valueOf(intentExtras.get("timezone")));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String dayOfWeek = days[position].getDayOfTheWeek();
        String conditions = days[position].getSummary();
        String highTemp = days[position].getTemperatureMax() + "";
        String message = String.format("On %s the high will be %s degrees and it will be %s",
                dayOfWeek,
                highTemp,
                conditions);
        Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_LONG).show();

    }
}
