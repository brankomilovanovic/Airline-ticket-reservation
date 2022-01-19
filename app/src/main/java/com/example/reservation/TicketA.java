package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.R;
import com.example.singiair.User;
import com.example.tickets.TicketReservation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TicketA extends AppCompatActivity implements Serializable {

    private User user;
    private int current_position_in_list;
    ImageView ticket_a_right, ticket_a_left;
    TextView ticket_a_label, ticket_a_departure_country, ticket_a_departure_city, ticket_a_flight_for_country, ticket_a_flight_for_city,
             ticket_a_passenger_name_surname, ticket_a_passenger_nationality, ticket_a_departure_date, ticket_a_departure_gate_closes,
             ticket_a_passport_number, ticket_a_departure_time;

    private List<TicketReservation> allCheckInTicket = new ArrayList<>();
    private List<TicketReservation> uniqueCheckInTicket = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_a);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        current_position_in_list = (int) intent.getSerializableExtra("current_position_in_list");

        updateAllTicketReservationList();

        String[] dateAndTime = uniqueCheckInTicket.get(current_position_in_list).getTicketReservationDepartureDateAndTime().split(" ");
        String[] departureTimeHourAndMinute = dateAndTime[1].replace(" ", "").split(":");
        int gateCloses = Integer.parseInt(departureTimeHourAndMinute[0]) - 1;

        String[] departureCountryAndCity = uniqueCheckInTicket.get(current_position_in_list).getTicketReservationDepartureFrom().split(",");
        String[] flightForCountryAndCity = uniqueCheckInTicket.get(current_position_in_list).getTicketReservationFlightFor().split(",");

        ticket_a_label = findViewById(R.id.ticket_a_label);
        ticket_a_label.setText(String.valueOf(uniqueCheckInTicket.get(current_position_in_list).getTicketReservationID()));

        ticket_a_departure_country = findViewById(R.id.ticket_b_departure_country);
        if(departureCountryAndCity[0].equals("Bosna i Hercegovina")){ departureCountryAndCity[0] = "BiH"; }
        if(departureCountryAndCity[0].equals("Ujedinjeno Kraljevstvo")){ departureCountryAndCity[0] = "UK";}
        if(departureCountryAndCity[0].equals("Severna Makedonija")){ departureCountryAndCity[0] = "Makedonija";}
        ticket_a_departure_country.setText(departureCountryAndCity[0]);

        ticket_a_departure_city = findViewById(R.id.ticket_b_departure_city);
        ticket_a_departure_city.setText(departureCountryAndCity[1].replace(" ", ""));

        ticket_a_flight_for_country = findViewById(R.id.ticket_b_flight_for_country);
        if(flightForCountryAndCity[0].equals("Bosna i Hercegovina")){ flightForCountryAndCity[0] = "BiH"; }
        if(flightForCountryAndCity[0].equals("Ujedinjeno Kraljevstvo")){ flightForCountryAndCity[0] = "UK";}
        if(flightForCountryAndCity[0].equals("Severna Makedonija")){ flightForCountryAndCity[0] = "Makedonija";}
        ticket_a_flight_for_country.setText(flightForCountryAndCity[0]);

        ticket_a_flight_for_city = findViewById(R.id.ticket_b_flight_for_city);
        ticket_a_flight_for_city.setText(flightForCountryAndCity[1].replace(" ", ""));

        ticket_a_passenger_name_surname = findViewById(R.id.ticket_b_passenger_name_surname);
        ticket_a_passenger_name_surname.setText(uniqueCheckInTicket.get(current_position_in_list).getTicketReservationCheckInUserSurname() + " " + uniqueCheckInTicket.get(current_position_in_list).getTicketReservationCheckInUserName());

        ticket_a_passenger_nationality = findViewById(R.id.ticket_b_passenger_nationality);
        ticket_a_passenger_nationality.setText(uniqueCheckInTicket.get(current_position_in_list).getTicketReservationCheckInUserNationality());

        ticket_a_departure_date = findViewById(R.id.ticket_b_departure_date);
        ticket_a_departure_date.setText(dateAndTime[0]);

        ticket_a_departure_gate_closes = findViewById(R.id.ticket_b_departure_gate_closes);
        ticket_a_departure_gate_closes.setText(String.valueOf(gateCloses) + ":" + departureTimeHourAndMinute[1] + " " + dateAndTime[2]);

        ticket_a_passport_number = findViewById(R.id.ticket_b_passport_number);
        ticket_a_passport_number.setText(uniqueCheckInTicket.get(current_position_in_list).getTicketReservationCheckInUserPassportNumber());

        ticket_a_departure_time = findViewById(R.id.ticket_b_departure_time);
        ticket_a_departure_time.setText(dateAndTime[1].replace(" ", "") + " " + dateAndTime[2]);

        ticket_a_right = findViewById(R.id.ticket_a_next_button);
        ticket_a_left = findViewById(R.id.ticket_a_back_button);

        if(current_position_in_list == 0) {
            ticket_a_left.setVisibility(View.INVISIBLE);
            ticket_a_left.setEnabled(false);
        }

        if((uniqueCheckInTicket.size() > current_position_in_list + 1) == false) {
            ticket_a_right.setVisibility(View.INVISIBLE);
            ticket_a_right.setEnabled(false);
        }

        ticket_a_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketA.this, TicketB.class);
                intent.putExtra("user", user);
                intent.putExtra("current_position_in_list", current_position_in_list - 1);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        ticket_a_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketA.this, TicketB.class);
                intent.putExtra("user", user);
                intent.putExtra("current_position_in_list", current_position_in_list + 1);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(TicketA.this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void updateAllTicketReservationList() {
        DBHelper MyDB = new DBHelper(TicketA.this);
        Cursor cursor = MyDB.getData("SELECT * FROM ticketsReserved");
        allCheckInTicket.clear();
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
            allCheckInTicket.add(new TicketReservation(id, id_user, id_flight, ticket_class, departureFrom, flightFor, dateAndTimeDeparture, check_in_user_name, check_in_user_surname, check_in_user_passport_number, check_in_user_nationality, checkIn, ticketPrice));
        }
        getSelectedCheckInTicket();
    }

    private void getSelectedCheckInTicket() {
        uniqueCheckInTicket.clear();
        for (int i = 0; i < allCheckInTicket.size(); i++) {
            if ((allCheckInTicket.get(i).getTicketReservationIDUser() == user.getUserId()) && (allCheckInTicket.get(i).getTicketReservationCheckIn() == true)) { // Get equals departure country and city
                uniqueCheckInTicket.add(allCheckInTicket.get(i));
            }
        }
    }
}