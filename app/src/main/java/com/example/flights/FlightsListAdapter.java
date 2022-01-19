package com.example.flights;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.singiair.R;
import com.example.singiair.User;

import java.util.ArrayList;

public class FlightsListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Flights> allFlights;

    public FlightsListAdapter(Context context, int layout, ArrayList<Flights> allFlights){
        this.context = context;
        this.layout = layout;
        this.allFlights = allFlights;
    }

    @Override
    public int getCount() {
        return allFlights.size();
    }

    public void filteredList(ArrayList<Flights> filterList) {
        allFlights = filterList;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return allFlights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView departureCountryAndCity, flightForCountryAndCity, dateAndTimeDeparture;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FlightsListAdapter.ViewHolder holder = new FlightsListAdapter.ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.departureCountryAndCity = row.findViewById(R.id.allFlightsDepartureCountryAndCityShow);
            holder.flightForCountryAndCity = row.findViewById(R.id.allFlightsForCountryAndCityShow);
            holder.dateAndTimeDeparture = row.findViewById(R.id.allFlightsDateAndTimeDeparture);
            row.setTag(holder);
        }
        else {
            holder = (FlightsListAdapter.ViewHolder) row.getTag();
        }

        Flights flights = allFlights.get(position);

        switch (flights.getFlightDepartureCountry()) {
            case "Bosna i Hercegovina":
                flights.setFlightDepartureCountry("BiH");
                break;
            case "Ujedinjeno Kraljevstvo":
                flights.setFlightDepartureCountry("UK");
                break;
            case "Severna Makedonija":
                flights.setFlightDepartureCountry("Makedonija");
                break;
        }

        holder.departureCountryAndCity.setText(flights.getFlightDepartureCountry() + ", " + flights.getFlightDepartureCity());
        holder.flightForCountryAndCity.setText(flights.getFlightForCountry() + ", " + flights.getFlightForCity());
        holder.dateAndTimeDeparture.setText(flights.getFlightDepartureDateAndTime());

        return row;
    }
}
