package com.example.tickets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.flights.Flights;
import com.example.singiair.DBHelper;
import com.example.singiair.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SearchFlightFragment extends Fragment {

    TextView setSearchFlightFromLabel, setSearchFlightForLabel, setSearchFlightForDateAndTime, setSearchFlightFromDateAndTime;
    LinearLayout flightForInput, flightFromInput, flightForDateAndTime, flightFromDateAndTime;
    Button searchFlight;

    private SimpleDateFormat dateTimeFormat;
    private Calendar calendar;

    //check which button is pressed
    boolean clickedDepartureDateTime = false;
    boolean clickedFlightForeDateTime = false;

    ArrayList<Flights> allFlights;
    String departureCountryAndCity, flightForCountryAndCity, departureDateTime, fightReturnDate;

    private Context context;
    private int User_id;

    public SearchFlightFragment(Context context, int user_id){
        this.context=context;
        this.User_id=user_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_search_flight, container, false);

        //Get values from another bundle
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            departureCountryAndCity = bundle.getString("departureCountryAndCity");
            departureDateTime = bundle.getString("departureDateTime");
            flightForCountryAndCity = bundle.getString("flightForCountryAndCity");
            fightReturnDate = bundle.getString("fightReturnDate");
        }

        //Start and end destination
        setSearchFlightFromLabel = (TextView) rootView.findViewById(R.id.searchFlightFromLabel);
        if(departureCountryAndCity != null) { setSearchFlightFromLabel.setText(departureCountryAndCity); }
        setSearchFlightForLabel = (TextView) rootView.findViewById(R.id.searchFlightForLabel);
        if(flightForCountryAndCity != null) { setSearchFlightForLabel.setText(flightForCountryAndCity); }

        //Time for flight
        setSearchFlightForDateAndTime = (TextView) rootView.findViewById(R.id.searchFlightForDateAndTimeLabel);
        if(fightReturnDate != null) { setSearchFlightForDateAndTime.setText(fightReturnDate); }
        setSearchFlightFromDateAndTime = (TextView) rootView.findViewById(R.id.searchFlightFromDateAndTimeLabel);
        if(departureDateTime != null) { setSearchFlightFromDateAndTime.setText(departureDateTime); }

        //Set linear layout like button
        flightFromInput = (LinearLayout) rootView.findViewById(R.id.searchFlightFromInput);
        flightForInput = (LinearLayout) rootView.findViewById(R.id.searchFlightForInput);
        flightForDateAndTime = (LinearLayout) rootView.findViewById(R.id.searchFlightForDateAndTime);
        flightFromDateAndTime = (LinearLayout) rootView.findViewById(R.id.searchFlightFromDateAndTime);

        dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        allFlights = new ArrayList<>();
        updateAllFlightsList();

        //Search flight button
        searchFlight = rootView.findViewById(R.id.searchFlightButton);

        flightFromInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departureCountryAndCity, flightForCountryAndCity;
                String flightFromDateAndTime, flightForDateAndTime;

                departureCountryAndCity = setSearchFlightFromLabel.getText().toString();
                flightForCountryAndCity = setSearchFlightForLabel.getText().toString();
                flightFromDateAndTime = setSearchFlightFromDateAndTime.getText().toString();
                flightForDateAndTime = setSearchFlightForDateAndTime.getText().toString();

                SearchDepartureFlight searchTicketFragment = new SearchDepartureFlight();
                final Bundle bundle = new Bundle();
                bundle.putString("departureCountryAndCity", departureCountryAndCity);
                bundle.putString("departureDateTime", flightFromDateAndTime);
                bundle.putString("flightForCountryAndCity", flightForCountryAndCity);
                bundle.putString("fightReturnDate", flightForDateAndTime);
                bundle.putInt("user_id", User_id);
                searchTicketFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        //.remove(searchTicketFragment)
                        .replace(((ViewGroup) getView().getParent()).getId(), searchTicketFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });

        flightForInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!setSearchFlightFromLabel.getText().toString().equals("Flight from")) {
                    String departureCountryAndCity, flightForCountryAndCity;
                    String flightFromDateAndTime, flightForDateAndTime;

                    departureCountryAndCity = setSearchFlightFromLabel.getText().toString();
                    flightForCountryAndCity = setSearchFlightForLabel.getText().toString();
                    flightFromDateAndTime = setSearchFlightFromDateAndTime.getText().toString();
                    flightForDateAndTime = setSearchFlightForDateAndTime.getText().toString();

                    SearchFlightFor searchFlightForFragment = new SearchFlightFor();
                    final Bundle bundle = new Bundle();
                    bundle.putString("departureCountryAndCity", departureCountryAndCity);
                    bundle.putString("departureDateTime", flightFromDateAndTime);
                    bundle.putString("flightForCountryAndCity", flightForCountryAndCity);
                    bundle.putString("fightReturnDate", flightForDateAndTime);
                    bundle.putInt("user_id", User_id);
                    searchFlightForFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(((ViewGroup) getView().getParent()).getId(), searchFlightForFragment, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "You must select the city of departure", Toast.LENGTH_SHORT).show();
                }
            }
        });

        flightForDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(!setSearchFlightFromDateAndTime.getText().toString().equals("Date departure")) {
                    calendar = Calendar.getInstance();
                    clickedFlightForeDateTime = true;
                    clickedDepartureDateTime = false;
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, mDateDataSet, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                } else {
                    Toast.makeText(getContext(), "You must select a departure time", Toast.LENGTH_SHORT).show();
                }*/
                Toast.makeText(getContext(), "This feature is currently disabled.", Toast.LENGTH_SHORT).show();
            }
        });

        flightFromDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!setSearchFlightFromLabel.getText().toString().equals("Flight from") && !setSearchFlightForLabel.getText().toString().equals("Flight for")) {
                    calendar = Calendar.getInstance();

                    clickedDepartureDateTime = true;
                    clickedFlightForeDateTime = false;
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, mDateDataSet, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();

                } else {
                    Toast.makeText(getContext(), "You must select the city of departure and arrival", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] departureCountryAndCity, flightForCountryAndCity;
                String flightFromDateAndTime, flightForDateAndTime;
                if(!setSearchFlightFromLabel.getText().toString().equals("Flight from")) {
                    if(!setSearchFlightFromLabel.getText().toString().equals("Flight from")){
                        departureCountryAndCity = setSearchFlightFromLabel.getText().toString().split(",");
                    } else {
                        departureCountryAndCity = new String[] {"Null", " Null"};
                    }
                    if(!setSearchFlightForLabel.getText().toString().equals("Flight for")){
                        flightForCountryAndCity = setSearchFlightForLabel.getText().toString().split(",");
                    } else {
                        flightForCountryAndCity = new String[] {"Null", " Null"};
                    }
                    if(!setSearchFlightFromDateAndTime.getText().toString().equals("Date departure")){
                        flightFromDateAndTime = setSearchFlightFromDateAndTime.getText().toString();
                    } else {
                        flightFromDateAndTime = "Null";
                    }
                    if(!setSearchFlightForDateAndTime.getText().toString().equals("Return date")){
                        flightForDateAndTime = setSearchFlightForDateAndTime.getText().toString();
                    } else {
                        flightForDateAndTime = "Null";
                    }
                    SearchTicketFragment searchTicketFragment = new SearchTicketFragment();
                    final Bundle bundle = new Bundle();
                    bundle.putString("departureCountry", departureCountryAndCity[0]);
                    bundle.putString("departureCity", departureCountryAndCity[1].replace(" ", "")); // Replace whitespace
                    bundle.putString("departureDateTime", flightFromDateAndTime);
                    bundle.putString("flightForCountry", flightForCountryAndCity[0]);
                    bundle.putString("flightForCity", flightForCountryAndCity[1].replace(" ", "")); // Replace whitespace
                    bundle.putString("fightReturnDate", flightForDateAndTime);
                    bundle.putInt("user_id", User_id);
                    searchTicketFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(((ViewGroup) getView().getParent()).getId(), searchTicketFragment, "findThisFragment")
                            .addToBackStack(null)
                            .commit();

                } else {
                    Toast.makeText(getContext(), "You must select the city of departure", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    /* After user decided on a date, store those in our calendar variable and then start the TimePickerDialog immediately */
    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(clickedDepartureDateTime == true){
                setSearchFlightFromDateAndTime.setText(dateTimeFormat.format(calendar.getTime()));
            }
            else if(clickedFlightForeDateTime == true){
                setSearchFlightForDateAndTime.setText(dateTimeFormat.format(calendar.getTime()));
            }
        }
    };

    private void updateAllFlightsList(){
        //get all user from sqlite
        DBHelper MyDB = new DBHelper(context);
        Cursor cursor = MyDB.getData("SELECT * FROM flights");
        allFlights.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String departureCountry = cursor.getString(1);
            String departureCity = cursor.getString(2);
            String departureDateTime = cursor.getString(3);
            String flightForCountry = cursor.getString(4);
            String flightForCity = cursor.getString(5);
            String flightForDateTime = cursor.getString(6);
            double businessTicket = cursor.getInt(7);
            double firstClassTicket = cursor.getInt(8);
            int businessTicketNumber = Integer.valueOf(cursor.getString(9));
            int firstClassTicketNumber = Integer.valueOf(cursor.getString(10));
            //add to list
                allFlights.add(new Flights(id, departureCountry, departureCity,  flightForCountry, flightForCity, departureDateTime, flightForDateTime, businessTicket, firstClassTicket, businessTicketNumber, firstClassTicketNumber));
        }
    }
}
