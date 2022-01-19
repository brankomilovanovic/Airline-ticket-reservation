package com.example.tickets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admins.AdminsUserADD;
import com.example.admins.AdminsUserEDIT;
import com.example.admins.AdminsUsersCRUD;
import com.example.flights.Flights;
import com.example.flights.FlightsCRUD;
import com.example.profile.ProfileActivity;
import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.LoginActivity;
import com.example.singiair.R;
import com.example.singiair.User;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchTicketFragment extends Fragment {

    private DBHelper MyDB;
    private List<Flights> allFlightsList;
    private List<Flights> uniqueFlightsList = new ArrayList<>();
    private ListTicketAdapter listTicketAdapter;
    String departureCountry, departureCity, flightForCountry, flightForCity, departureDateTime, fightReturnDate;
    int user_id;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Get values from another bundle
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            departureCountry = bundle.getString("departureCountry");
            departureCity = bundle.getString("departureCity");
            departureDateTime = bundle.getString("departureDateTime");
            flightForCountry = bundle.getString("flightForCountry");
            flightForCity = bundle.getString("flightForCity");
            fightReturnDate = bundle.getString("fightReturnDate");
            user_id = bundle.getInt("user_id");
        }

        if(departureCountry.equals("BiH")){
            departureCountry = "Bosna i Hercegovina";
        }
        else if(departureCountry.equals("UK")){
            departureCountry = "Ujedinjeno Kraljevstvo";
        }
        else if(departureCountry.equals("Makedonija")){
            departureCountry = "Severna Makedonija";
        }

        View mview = inflater.inflate(R.layout.fragment_search_ticket, container, false);

        ListView listView = (ListView) mview.findViewById(R.id.lv_ticket);
        allFlightsList = new ArrayList<>();
        MyDB = new DBHelper(getContext());

        listTicketAdapter = new ListTicketAdapter(getContext(),R.layout.row_ticket, uniqueFlightsList);
        listView.setAdapter(listTicketAdapter);

        updateAllFlightsList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CharSequence[] items = {"Business class", "First class"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), user_id);
                dialog.setTitle("Buy ticket");
                dialog.setIcon(R.drawable.buy_ticket);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            showDialogBuyTicket(user_id, uniqueFlightsList.get(position).get_id(), "businessTicket", uniqueFlightsList.get(position).getFlightDepartureCountry() + ", " + uniqueFlightsList.get(position).getFlightDepartureCity(),
                                    uniqueFlightsList.get(position).getFlightForCountry() + ", " + uniqueFlightsList.get(position).getFlightForCity(), uniqueFlightsList.get(position).getFlightDepartureDateAndTime(), uniqueFlightsList.get(position).getFlightBusinessTicket(),
                                    uniqueFlightsList.get(position).getFlightBusinessTicketNumber());
                        }
                        if (which == 1){
                            showDialogBuyTicket(user_id, uniqueFlightsList.get(position).get_id(), "firstClassTicket", uniqueFlightsList.get(position).getFlightDepartureCountry() + ", " + uniqueFlightsList.get(position).getFlightDepartureCity(),
                                uniqueFlightsList.get(position).getFlightForCountry() + ", " + uniqueFlightsList.get(position).getFlightForCity(), uniqueFlightsList.get(position).getFlightDepartureDateAndTime(), uniqueFlightsList.get(position).getFlightFirstClassTicket(),
                                    uniqueFlightsList.get(position).getFlightFirstClassTicketNumber());
                        }
                    }
                });
                dialog.show();
            }
        });
        return mview;
    }

    private void showDialogBuyTicket(int id_user, int id_flight, String classTicket, String departureFrom, String flightFor, String departureDateAndTime, double ticketPrice, int ticketNumberClass) {
        final EditText taskEditText = new EditText(getContext());
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), user_id);
        dialog.setTitle("Buy ticket");
        dialog.setIcon(R.drawable.buy_ticket);
        dialog.setMessage("Enter the number of cards you want\nOne ticket price: $" + ticketPrice);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        taskEditText.setText("1");
        dialog.setView(taskEditText);
        dialog.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ticketNumber = String.valueOf(taskEditText.getText());
                if(ticketNumber.equals("") || ticketNumber.equals("0")){
                    Toast.makeText(getContext(), "Error!\nYou did not enter a ticket number!", Toast.LENGTH_SHORT).show();
                } else {
                    if(ticketNumberClass >= Integer.valueOf(ticketNumber)) {
                        User user = MyDB.getUserData(id_user);
                        if (user.getUserMoney() < (ticketPrice * Integer.valueOf(ticketNumber))) {
                            Toast.makeText(getContext(), "Error!\nYou do not have enough money in your account.", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < Integer.parseInt(ticketNumber); i++) {
                                MyDB.insertDataTicketReservation(id_user, id_flight, classTicket, departureFrom, flightFor, departureDateAndTime, null, null, null, null, false, ticketPrice);
                            }
                            MyDB.updateUserMoney(user.getUserId(), user.getUserMoney() - (ticketPrice * Integer.valueOf(ticketNumber)));
                            Toast.makeText(getContext(), "You have successfully booked " + ticketNumber + " tickets.\nYou paid in total: $" + (ticketPrice * Integer.valueOf(ticketNumber)), Toast.LENGTH_SHORT).show();
                            if (classTicket.equals("firstClassTicket")) {
                                MyDB.updateFirstClassTicketNumber(id_flight, (ticketNumberClass - Integer.valueOf(ticketNumber)));
                            } else {
                                MyDB.updateBusinessTicketNumber(id_flight, (ticketNumberClass - Integer.valueOf(ticketNumber)));
                            }
                            updateAllFlightsList();
                        }
                    } else {
                        Toast.makeText(getContext(), "There are not enough free tickets to sell.\n\nFree tickets: " + ticketNumberClass, Toast.LENGTH_LONG).show();
                    }
                }
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

    private void getSelectedFlights(){
        uniqueFlightsList.clear();
        if(!fightReturnDate.equals("Null")){ //Selected departure, flight for, departure date and return date
            for(int i = 0; i < allFlightsList.size(); i++) {
                if(allFlightsList.get(i).getFlightDepartureCountry().equals(departureCountry) && allFlightsList.get(i).getFlightDepartureCity().equals(departureCity) &&
                        allFlightsList.get(i).getFlightForCountry().equals(flightForCountry) && allFlightsList.get(i).getFlightForCity().equals(flightForCity) &&
                            allFlightsList.get(i).getFlightDepartureDateAndTime().split("\\s+")[0].equals(departureDateTime) && allFlightsList.get(i).getFlightForDateAndTime().split("\\s+")[0].equals(fightReturnDate)) {
                                uniqueFlightsList.add(allFlightsList.get(i));
                }
            }
        }
        else if (!departureDateTime.equals("Null")){ //Selected departure, flight for and departure date
            for(int i = 0; i < allFlightsList.size(); i++) {
                if(allFlightsList.get(i).getFlightDepartureCountry().equals(departureCountry) && allFlightsList.get(i).getFlightDepartureCity().equals(departureCity) &&
                        allFlightsList.get(i).getFlightForCountry().equals(flightForCountry) && allFlightsList.get(i).getFlightForCity().equals(flightForCity) &&
                            allFlightsList.get(i).getFlightDepartureDateAndTime().split("\\s+")[0].equals(departureDateTime)) {
                                uniqueFlightsList.add(allFlightsList.get(i));
                }
            }
        }
        else if (!flightForCountry.equals("Null")){ //Selected departure and flight for
            for(int i = 0; i < allFlightsList.size(); i++) {
                if(allFlightsList.get(i).getFlightDepartureCountry().equals(departureCountry) && allFlightsList.get(i).getFlightDepartureCity().equals(departureCity) &&
                        allFlightsList.get(i).getFlightForCountry().equals(flightForCountry) && allFlightsList.get(i).getFlightForCity().equals(flightForCity)) {
                            uniqueFlightsList.add(allFlightsList.get(i));
                }
            }
        }
        else {
            for(int i = 0; i < allFlightsList.size(); i++) {
                if(allFlightsList.get(i).getFlightDepartureCountry().equals(departureCountry) && allFlightsList.get(i).getFlightDepartureCity().equals(departureCity)) { // Get equals departure country and city
                    uniqueFlightsList.add(allFlightsList.get(i));
                }
            }
        }
    }

    private void updateAllFlightsList() {
        //get all flights from sqlite
        DBHelper MyDB = new DBHelper(getContext());
        Cursor cursor = MyDB.getData("SELECT * FROM flights");
        allFlightsList.clear();
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
            allFlightsList.add(new Flights(id, flightDepartureCountry, flightDepartureCity, flightForCountry, flightForCity, flightDepartureDateAndTime, flightForDateAndTime, flightBusinessTicket, flightFirstClassTicket, flightBusinessTicketNumber, flightFirstClassTicketNumber));
        }

        getSelectedFlights(); // get just selected flights (departure city, flight for city, date departure, return date)...
        listTicketAdapter.notifyDataSetChanged();
    }
}