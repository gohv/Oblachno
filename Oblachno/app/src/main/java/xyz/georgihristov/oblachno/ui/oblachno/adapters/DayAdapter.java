package xyz.georgihristov.oblachno.ui.oblachno.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import oblachno.xyz.georgihristov.ui.R;
import xyz.georgihristov.oblachno.ui.oblachno.model.Daily;

/**
 * Created by gohv on 10.09.16.
 */
public class DayAdapter extends BaseAdapter{
    private Context context;
    private Daily[] days;

    public DayAdapter(Context context, Daily[] days){
        this.context = context;
        this.days = days;
    }

    //get count of items in the array this adapter is using - the count of Daily array:
    @Override
    public int getCount() {
        return days.length;
    }
    //get the item of the array given the certain possition:
    @Override
    public Object getItem(int position) {
        return days[position];
    }
        // I wont be using this! But its used to tag items for easy reference!
    @Override
    public long getItemId(int position) {
        return 0;
    }
    // the mapping occurs here:!
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            // brand new
            convertView = LayoutInflater.from(context).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();

            holder.iconImageView =(ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel =(TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayNameLabel =(TextView) convertView.findViewById(R.id.dayNameLabel);


            //sets a tag for the view
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Daily day = days[position];

        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax() + "");



       if(position == 0) {
            holder.dayNameLabel.setText("Today");
        }else{
            holder.dayNameLabel.setText(day.getDayOfTheWeek());
        }


        return convertView;

    }
    private static class ViewHolder{
        ImageView iconImageView; //public by default
        TextView temperatureLabel;
        TextView dayNameLabel;

    }
}
