package com.example.tickets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.flights.Flights;
import com.example.singiair.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTicketReservationActivityAdapter extends ArrayAdapter {
    List<TicketReservation> ticketReservationsList;
    String[] departureCountryAndCity, flightForCountryAndCity;

    public ListTicketReservationActivityAdapter(Context context, int resource, List<TicketReservation> list)
    {
        super(context, resource);
        ticketReservationsList = list;
    }

    static class LayoutHandler{
        TextView departureCountry, departureCity, departureDateAndTime, flightForCountry, flightForCity, departureTimeLabel, idFlightTicket
                ,dollarSign1, dollarSign2, firstClassLabel, departureDateAndTimeLabel, firstClassPriceLabel, businessClassNumberTicketLabel, firstClassNumberTicketLabel;
    }

    @Override
    public int getCount() {
        return ticketReservationsList.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketReservationsList.get(position);
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
            layoutHandler.departureDateAndTime = (TextView)mview.findViewById(R.id.businessClassTicketShowPrice);
            layoutHandler.flightForCountry = (TextView)mview.findViewById(R.id.flightForCountryShowTicket);
            layoutHandler.flightForCity = (TextView)mview.findViewById(R.id.flightForCityShowTicket);
            layoutHandler.idFlightTicket = (TextView)mview.findViewById(R.id.idFlightShowTicket);
            layoutHandler.departureTimeLabel = (TextView)mview.findViewById(R.id.businessClassLabel);
            layoutHandler.dollarSign1 = mview.findViewById(R.id.dollarSignBusiness);
            layoutHandler.dollarSign2 = mview.findViewById(R.id.dollarSignFirst);
            layoutHandler.firstClassLabel = mview.findViewById(R.id.firstClassLabel);
            layoutHandler.departureDateAndTimeLabel = mview.findViewById(R.id.departureDateAndTimeShowTicket);
            layoutHandler.firstClassPriceLabel = mview.findViewById(R.id.firstClassTicketShowPrice);
            layoutHandler.businessClassNumberTicketLabel = mview.findViewById(R.id.businessClassNumberTicketLabel);
            layoutHandler.firstClassNumberTicketLabel = mview.findViewById(R.id.firstClassNumberTicketLabel);
            mview.setTag(layoutHandler);
        }else {
            layoutHandler = (LayoutHandler) mview.getTag();
        }

        TicketReservation ticketReservations = (TicketReservation) this.getItem(position);

        departureCountryAndCity = ticketReservations.getTicketReservationDepartureFrom().split(",");
        flightForCountryAndCity = ticketReservations.getTicketReservationFlightFor().split(",");

        if(departureCountryAndCity[0].equals("Bosna i Hercegovina")){
            departureCountryAndCity[0] = "BiH";
        }
        else if(departureCountryAndCity[0].equals("Ujedinjeno Kraljevstvo")){
            departureCountryAndCity[0] = "UK";
        }
        else if(departureCountryAndCity[0].equals("Severna Makedonija")){
            departureCountryAndCity[0] = "Makedonija";
        }

        layoutHandler.departureCountry.setText(departureCountryAndCity[0]);
        layoutHandler.departureCity.setText(departureCountryAndCity[1].replace(" ", ""));
        layoutHandler.departureDateAndTime.setText(ticketReservations.getTicketReservationDepartureDateAndTime());
        layoutHandler.flightForCountry.setText(flightForCountryAndCity[0]);
        layoutHandler.flightForCity.setText(flightForCountryAndCity[1].replace(" ", ""));
        layoutHandler.idFlightTicket.setText(String.valueOf(ticketReservations.getTicketReservationIDFlight()));
        layoutHandler.departureTimeLabel.setText("Departure:");
        layoutHandler.dollarSign1.setText("");
        layoutHandler.businessClassNumberTicketLabel.setText("");
        layoutHandler.firstClassNumberTicketLabel.setText("");
        if(ticketReservations.getTicketReservationCheckIn() == true){
            layoutHandler.dollarSign2.setText("Yes");
        } else {
            layoutHandler.dollarSign2.setText("No");
        }
        layoutHandler.firstClassLabel.setText("Check in:");
        layoutHandler.departureDateAndTimeLabel.setText("");
        layoutHandler.firstClassPriceLabel.setText("");
        return mview;
    }
}