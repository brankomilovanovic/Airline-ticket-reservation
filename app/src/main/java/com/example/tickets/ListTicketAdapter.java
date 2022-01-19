package com.example.tickets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.flights.Flights;
import com.example.singiair.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTicketAdapter extends ArrayAdapter {
    List<Flights> flightsList;

    public ListTicketAdapter(Context context, int resource, List<Flights> list)
    {
        super(context, resource);
        flightsList = list;
    }

    static class LayoutHandler{
        TextView departureCountry, departureCity, departureDateAndTime, flightForCountry, flightForCity, businessClassTicket, firstClassTicket, idFlightTicket, businessClassTicketNumber, firstClassTicketNumber;
    }

    @Override
    public int getCount() {
        return flightsList.size();
    }

    @Override
    public Object getItem(int position) {
        return flightsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mview = convertView;
        LayoutHandler layoutHandler;
        if(mview==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mview = layoutInflater.inflate(R.layout.row_ticket,parent,false);
            layoutHandler = new LayoutHandler();
            layoutHandler.departureCountry = (TextView)mview.findViewById(R.id.departureCountryShowTicket);
            layoutHandler.departureCity = (TextView)mview.findViewById(R.id.departureCityShowTicket);
            layoutHandler.departureDateAndTime = (TextView)mview.findViewById(R.id.departureDateAndTimeShowTicket);
            layoutHandler.flightForCountry = (TextView)mview.findViewById(R.id.flightForCountryShowTicket);
            layoutHandler.flightForCity = (TextView)mview.findViewById(R.id.flightForCityShowTicket);
            layoutHandler.businessClassTicket = (TextView)mview.findViewById(R.id.businessClassTicketShowPrice);
            layoutHandler.firstClassTicket = (TextView)mview.findViewById(R.id.firstClassTicketShowPrice);
            layoutHandler.idFlightTicket = (TextView)mview.findViewById(R.id.idFlightShowTicket);
            layoutHandler.businessClassTicketNumber = (TextView)mview.findViewById(R.id.businessClassNumberTicketLabel);
            layoutHandler.firstClassTicketNumber = (TextView)mview.findViewById(R.id.firstClassNumberTicketLabel);
            mview.setTag(layoutHandler);
        }else {
            layoutHandler = (LayoutHandler) mview.getTag();
        }
        Flights flights = (Flights) this.getItem(position);
        String flightDepartureCountry = flights.getFlightDepartureCountry();
        if(flightDepartureCountry.equals("Bosna i Hercegovina")){
            flightDepartureCountry = "BiH";
        }
        else if(flightDepartureCountry.equals("Ujedinjeno Kraljevstvo")){
            flightDepartureCountry = "UK";
        }
        else if(flightDepartureCountry.equals("Severna Makedonija")){
            flightDepartureCountry = "Makedonija";
        }
        layoutHandler.departureCountry.setText(flightDepartureCountry);
        layoutHandler.departureCity.setText(flights.getFlightDepartureCity());
        layoutHandler.departureDateAndTime.setText(flights.getFlightDepartureDateAndTime());
        layoutHandler.flightForCountry.setText(flights.getFlightForCountry());
        layoutHandler.flightForCity.setText(flights.getFlightForCity());
        layoutHandler.businessClassTicket.setText(String.valueOf(flights.getFlightBusinessTicket()));
        layoutHandler.firstClassTicket.setText(String.valueOf(flights.getFlightFirstClassTicket()));
        layoutHandler.idFlightTicket.setText(String.valueOf(flights.get_id()));
        layoutHandler.businessClassTicketNumber.setText(flights.getFlightBusinessTicketNumber() + ")");
        layoutHandler.firstClassTicketNumber.setText(flights.getFlightFirstClassTicketNumber() + ")");
        return mview;
    }
}