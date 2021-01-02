package res.managit.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import res.managit.R;
import res.managit.dbo.entity.Event;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_row_event, parent, false);
        }
        TextView action = (TextView) convertView.findViewById(R.id.action);
        TextView date = (TextView) convertView.findViewById(R.id.date);


        String textAction = "Type: " + event.action;
        String textDate = "Date " + event.date.getYear() + " " + event.getDate().getMonth() + " " + event.getDate().getDayOfWeek() + " " + event.getDate().getHour() + ":" + event.getDate().getMinute();
        if (event.getDate().getMinute() == 0)
            textDate += "0";

        action.setText(textAction);
        date.setText(textDate);

        return convertView;
    }


}
