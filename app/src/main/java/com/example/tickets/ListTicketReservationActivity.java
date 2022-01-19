package com.example.tickets;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.flights.Flights;
import com.example.flights.FlightsCRUD;
import com.example.reservation.TicketA;
import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.R;
import com.example.singiair.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class ListTicketReservationActivity extends AppCompatActivity {

    private User user;
    private ListTicketReservationActivityAdapter listTicketReservationActivityAdapter;
    private List<TicketReservation> allTicketReservationList = new ArrayList<>();
    private List<TicketReservation> uniqueTicketReservationList = new ArrayList<>();
    DBHelper MyDB = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ticket_reservation);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        ListView listView = findViewById(R.id.list_view_ticket_reservation);
        allTicketReservationList = new ArrayList<>();

        listTicketReservationActivityAdapter = new ListTicketReservationActivityAdapter(ListTicketReservationActivity.this,R.layout.row_ticket, uniqueTicketReservationList);
        listView.setAdapter(listTicketReservationActivityAdapter);

        updateAllTicketReservationList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(uniqueTicketReservationList.get(position).getTicketReservationCheckIn() == true){
                    Toast.makeText(ListTicketReservationActivity.this, "The card is checked!", Toast.LENGTH_SHORT).show();
                } else {
                    CharSequence[] items = {"Check in reservation", "Cancel reservation"};
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ListTicketReservationActivity.this);
                    dialog.setTitle("Reservation ticket");
                    dialog.setIcon(R.drawable.list_icon);
                    dialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0){
                                Intent intent = new Intent(ListTicketReservationActivity.this, CheckInTicketActivity.class);
                                intent.putExtra("ticketReservationID", uniqueTicketReservationList.get(position).getTicketReservationID());
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                            }
                            if (which == 1){
                                showDialogCancelTicket(uniqueTicketReservationList.get(position).getTicketReservationID());
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private void showDialogCancelTicket(int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ListTicketReservationActivity.this);
        TicketReservation ticketReservation = MyDB.getTicketReservationData(id);
        double refunds = ticketReservation.getTicketPrice() / 2;
        dialog.setTitle("Cancel reservation!");
        dialog.setIcon(R.drawable.list_icon);
        dialog.setMessage("Are you sure to cancel reservation?\n\nIf you cancel the reservation you get a refund of 50% of the ticket price.\n\nRefunds: " + refunds);
        DBHelper MyDB = new DBHelper(ListTicketReservationActivity.this);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Delete reservation
                try{
                    MyDB.deleteReservationTicket(id);
                    MyDB.updateUserMoney(user.getUserId(), (user.getUserMoney() + refunds));
                    Toast.makeText(ListTicketReservationActivity.this, "You have successfully canceled your flight reservation.", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("Error!", e.getMessage());
                }
                updateAllTicketReservationList();
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

    private void updateAllTicketReservationList() {
        DBHelper MyDB = new DBHelper(ListTicketReservationActivity.this);
        Cursor cursor = MyDB.getData("SELECT * FROM ticketsReserved");
        allTicketReservationList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int id_user = cursor.getInt(1);
            int id_flight = cursor.getInt(2);
            String ticket_class = cursor.getString(3);
            String departureFrom = cursor.getString(4);
            String flightFor = cursor.getString(5);
            String dateAndTimeDeparture = cursor.getString(6);
            String check_in_user_name = cursor.getString(7);
            String check_in_user_surname = cursor.getString(8);
            String check_in_user_passport_number = cursor.getString(9);
            String check_in_user_nationality = cursor.getString(10);
            boolean checkIn = cursor.getInt(11) > 0;
            double ticketPrice = cursor.getDouble(12);
            //add to list
            allTicketReservationList.add(new TicketReservation(id, id_user, id_flight, ticket_class, departureFrom, flightFor, dateAndTimeDeparture, check_in_user_name, check_in_user_surname, check_in_user_passport_number, check_in_user_nationality, checkIn, ticketPrice));
        }
        getSelectedReservation();
        listTicketReservationActivityAdapter.notifyDataSetChanged();
    }

    private void getSelectedReservation() {
        uniqueTicketReservationList.clear();
        for (int i = 0; i < allTicketReservationList.size(); i++) {
            if (allTicketReservationList.get(i).getTicketReservationIDUser() == user.getUserId() && allTicketReservationList.get(i).getTicketReservationCheckIn() == false) { // Get equals departure country and city
                uniqueTicketReservationList.add(allTicketReservationList.get(i));
            }
        }
        if(uniqueTicketReservationList.size() == 0){
            Intent intent = new Intent(ListTicketReservationActivity.this, HomeActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(ListTicketReservationActivity.this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}