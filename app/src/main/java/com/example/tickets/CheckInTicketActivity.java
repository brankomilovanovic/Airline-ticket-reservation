package com.example.tickets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.LoginActivity;
import com.example.singiair.R;
import com.example.singiair.RegistrationActivity;
import com.example.singiair.User;

import java.util.ArrayList;
import java.util.List;

public class CheckInTicketActivity extends AppCompatActivity {

    private int ticketReservationID;
    private TicketReservation ticketReservation;
    private User user;

    TextView checkInTicketLabel;
    Button checkInTicketSubmit, checkInCancel;
    EditText departureFromLabel, flightForLabel, passengerName, passengerSurname, passengerPassportNumber, passengerNationality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_ticket);

        DBHelper MyDB = new DBHelper(this);

        Intent intent = getIntent();
        ticketReservationID = (int) intent.getSerializableExtra("ticketReservationID");
        user = (User) intent.getSerializableExtra("user");

        departureFromLabel = findViewById(R.id.checkInTicketDepartureInput);
        flightForLabel = findViewById(R.id.checkInTicketFlightForInput);
        passengerName = findViewById(R.id.checkInTicketNameInput);
        passengerName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        passengerSurname = findViewById(R.id.checkInTicketSurnameInput);
        passengerSurname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        passengerPassportNumber = findViewById(R.id.checkInTicketPassportInput);
        passengerNationality = findViewById(R.id.checkInTicketNationalityInput);
        passengerNationality.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        checkInTicketLabel = findViewById(R.id.checkInTicketLabel);
        checkInTicketSubmit = findViewById(R.id.checkInTicketSubmitButton);
        checkInCancel = findViewById(R.id.checkInTicketCancelButton);

        ticketReservation = MyDB.getTicketReservationData(ticketReservationID);

        checkInTicketLabel.setText("Check in ticket (ID: " + ticketReservation.getTicketReservationID() + ")");
        departureFromLabel.setText(ticketReservation.getTicketReservationDepartureFrom());
        departureFromLabel.setEnabled(false);
        flightForLabel.setText(ticketReservation.getTicketReservationFlightFor());
        flightForLabel.setEnabled(false);

        checkInCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckInTicketActivity.this, ListTicketReservationActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        checkInTicketSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = passengerName.getText().toString();
                String surname = passengerSurname.getText().toString();
                String passportNumber = passengerPassportNumber.getText().toString();
                String nationality = passengerNationality.getText().toString();

                if(name.equals("") || surname.equals("") || passportNumber.equals("") || nationality.equals("")){
                    Toast.makeText(CheckInTicketActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                    surname = surname.substring(0,1).toUpperCase() + surname.substring(1).toLowerCase();
                    nationality = nationality.substring(0,1).toUpperCase() + nationality.substring(1).toLowerCase();

                    MyDB.updateTicketReservationInfo(ticketReservationID, name, surname, passportNumber, nationality, true);
                    Toast.makeText(CheckInTicketActivity.this, "Check in ticket successful!", Toast.LENGTH_SHORT).show();
                    passengerName.setText("");
                    passengerSurname.setText("");
                    passengerPassportNumber.setText("");
                    passengerNationality.setText("");

                    Intent intent = new Intent(CheckInTicketActivity.this, ListTicketReservationActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckInTicketActivity.this, ListTicketReservationActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
        return;
    }
}