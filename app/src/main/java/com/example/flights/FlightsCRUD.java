package com.example.flights;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admins.AdminsUserEDIT;
import com.example.admins.AdminsUsersCRUD;
import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.R;
import com.example.singiair.User;

import java.util.ArrayList;

public class FlightsCRUD extends AppCompatActivity {

    ListView allFlightList;
    ArrayList<Flights> allFlight;
    ArrayList<Flights> allFlightFiltered;
    FlightsListAdapter flightAdapter = null;
    View addNewFlight;
    boolean filtered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights_crud);

        EditText searchFlight = findViewById(R.id.searchFlightsInput);

        addNewFlight = findViewById(R.id.flightCrudAddNewFlightButton);
        allFlightList = findViewById(R.id.flightCrudListView);

        allFlight = new ArrayList<>();
        flightAdapter = new FlightsListAdapter(this, R.layout.flights_crud, allFlight);
        allFlightList.setAdapter(flightAdapter);

        //get all data from sqlite
        DBHelper MyDB = new DBHelper(FlightsCRUD.this);
        updateAllFlightsList();
        flightAdapter.notifyDataSetChanged();

        if (allFlight.size() == 0){
            //if there is no flights in table of databases
            Toast.makeText(this,"No flights found", Toast.LENGTH_SHORT).show();
        }

        searchFlight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        allFlightList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(FlightsCRUD.this);
                dialog.setTitle("Editing flight action");
                dialog.setIcon(R.drawable.list_icon);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //Update flights
                            Flights flights;
                            if(filtered == true){ //if use search bar
                                flights = MyDB.getFlightsData(allFlightFiltered.get(position).get_id());
                            }
                            else {
                                flights = MyDB.getFlightsData(allFlight.get(position).get_id());
                            }
                            Intent intent = new Intent(FlightsCRUD.this, FlightsEDIT.class);
                            intent.putExtra("flights", flights);
                            startActivity(intent);
                            finish();
                        }
                        if (which == 1){
                            //Delete flights
                            if(filtered == true){ //if use search bar
                                showDialogDelete(allFlightFiltered.get(position).get_id(), allFlightFiltered.get(position).getFlightDepartureCountry() + ", " + allFlightFiltered.get(position).getFlightDepartureCity(), allFlightFiltered.get(position).getFlightForCountry() + ", " + allFlightFiltered.get(position).getFlightForCity());
                            }
                            else {
                                showDialogDelete(allFlight.get(position).get_id(), allFlight.get(position).getFlightDepartureCountry() + ", " + allFlight.get(position).getFlightDepartureCity(), allFlight.get(position).getFlightForCountry() + ", " + allFlight.get(position).getFlightForCity());
                            }
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        addNewFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlightsADD.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDialogDelete(int id, String departure, String flightFor) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(FlightsCRUD.this);
        dialog.setTitle("Delete flights!");
        dialog.setMessage("Are you sure to delete?\n\nID Flight: " + id  + "\nDeparture: " + departure + "\nFlight for: " + flightFor);
        DBHelper MyDB = new DBHelper(FlightsCRUD.this);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Delete flights
                try{
                    MyDB.deleteFlight(id);
                    Toast.makeText(FlightsCRUD.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                    if(filtered == true){
                        Intent intent = new Intent(getApplicationContext(), FlightsCRUD.class);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateAllFlightsList();
            }
        });

        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateAllFlightsList() {
        //get all flights from sqlite
        DBHelper MyDB = new DBHelper(FlightsCRUD.this);
        Cursor cursor = MyDB.getData("SELECT * FROM flights");
        allFlight.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String flightDepartureCountry = cursor.getString(1);
            String flightDepartureCity = cursor.getString(2);
            String flightDepartureDateAndTime = cursor.getString(3);
            String flightForCountry = cursor.getString(4);
            String flightForCity = cursor.getString(5);
            String flightForDateAndTime = cursor.getString(6);
            Double flightBusinessTicket = Double.valueOf(cursor.getString(7));
            Double flightFirstClassTicket = Double.valueOf(cursor.getString(8));
            int flightBusinessTicketNumber = Integer.valueOf(cursor.getString(9));
            int flightFirstClassTicketNumber = Integer.valueOf(cursor.getString(10));
            //add to list
            allFlight.add(new Flights(id, flightDepartureCountry, flightDepartureCity, flightForCountry, flightForCity, flightDepartureDateAndTime, flightForDateAndTime, flightBusinessTicket, flightFirstClassTicket, flightBusinessTicketNumber, flightFirstClassTicketNumber));
        }
        flightAdapter.notifyDataSetChanged();
    }

    private void filter(String text){
        ArrayList<Flights> filterList = new ArrayList<>();
        for (Flights flights: allFlight){

            String search_departure_country_and_city = flights.getFlightDepartureCountry() + " " + flights.getFlightDepartureCity();
            String search_departure_city_and_country = flights.getFlightDepartureCity() + " " + flights.getFlightDepartureCountry();
            if(flights.getFlightDepartureCountry().equals("BiH")) { search_departure_city_and_country = "Bosna i Hercegovina " + flights.getFlightDepartureCity(); search_departure_country_and_city = flights.getFlightDepartureCity() + " Bosna i Hercegovina"; }
            if(flights.getFlightDepartureCountry().equals("UK")) { search_departure_city_and_country = "Ujedinjeno Kraljevstvo " + flights.getFlightDepartureCity(); search_departure_country_and_city = flights.getFlightDepartureCity() + " Ujedinjeno Kraljevstvo"; }
            if(flights.getFlightDepartureCountry().equals("Makedonija")) { search_departure_city_and_country = "Severna Makedonija " + flights.getFlightDepartureCity(); search_departure_country_and_city = flights.getFlightDepartureCity() + " Severna Makedonija"; }

            String search_flight_for_country_and_city = flights.getFlightForCountry() + " " + flights.getFlightForCity();
            String search_flight_for_city_and_country = flights.getFlightForCity() + " " + flights.getFlightForCountry();
            if(flights.getFlightForCountry().equals("BiH")) { search_flight_for_country_and_city = "Bosna i Hercegovina " + flights.getFlightDepartureCountry(); search_flight_for_country_and_city = flights.getFlightForCity() + " Bosna i Hercegovina";}
            if(flights.getFlightForCountry().equals("UK")) { search_flight_for_country_and_city = "Ujedinjeno Kraljevstvo " + flights.getFlightDepartureCountry(); search_flight_for_country_and_city = flights.getFlightForCity() + " Ujedinjeno Kraljevstvo"; }
            if(flights.getFlightForCountry().equals("Makedonija")) { search_flight_for_country_and_city = "Severna Makedonija " + flights.getFlightDepartureCountry(); search_flight_for_country_and_city = flights.getFlightForCity() + " Severna Makedonija"; }

            if(flights.getFlightDepartureCountry().toLowerCase().contains(text.toLowerCase())){ // Get user just with name
                filterList.add(flights);
            }
            else if (search_departure_country_and_city.toLowerCase().contains(text.toLowerCase())) { // Get user with name and surname
                filterList.add(flights);
            }
            else if (flights.getFlightDepartureCity().toLowerCase().contains(text.toLowerCase())) { // Get user with name and surname
                filterList.add(flights);
            }
            else if (search_departure_city_and_country.toLowerCase().contains(text.toLowerCase())) { // Get user with name and surname
                filterList.add(flights);
            }
            else if(flights.getFlightForCountry().toLowerCase().contains(text.toLowerCase())){ // Get user just with name
                filterList.add(flights);
            }
            else if(search_flight_for_country_and_city.toLowerCase().contains(text.toLowerCase())){ // Get user just with name
                filterList.add(flights);
            }
            else if(flights.getFlightForCity().toLowerCase().contains(text.toLowerCase())){ // Get user just with name
                filterList.add(flights);
            }
            else if(search_flight_for_city_and_country.toLowerCase().contains(text.toLowerCase())){ // Get user just with name
                filterList.add(flights);
            }
            flightAdapter.filteredList(filterList);
            filtered = true;
            allFlightFiltered = filterList;
        }
    }
}