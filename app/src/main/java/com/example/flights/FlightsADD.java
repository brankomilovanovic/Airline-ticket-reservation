package com.example.flights;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admins.AdminsUserEDIT;
import com.example.admins.AdminsUsersCRUD;
import com.example.singiair.DBHelper;
import com.example.singiair.R;
import com.example.singiair.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FlightsADD extends AppCompatActivity {

    EditText addNewFlightDepartureCountry, addNewFlightDepartureCity, addNewFlightForCountry, addNewFlightForCity, addNewFlightPriceBusinessTicket,
             addNewFlightPriceFirstClassTicket, addNewFlightFirstClassTicketNumber, addNewFlightBusinessClassTicketNumber;
    Button addNewFlightDepartureDateAndTime, addNewFlightForDateAndTime, addNewFlightSaveButton, addNewFlightCancelButtom;
    TextView departureDateAndTimeLabel, flightForDateAndTimeLabel;

    public static DBHelper MyDB;

    private SimpleDateFormat dateTimeFormat;
    private Calendar calendar;

    //check which button is pressed
    boolean clickedDepartureDateTime = false;
    boolean clickedFlightForeDateTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights_add);

        addNewFlightDepartureCountry = findViewById(R.id.adminAddDepartureFromInputCountry);
        addNewFlightDepartureCountry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        addNewFlightDepartureCity = findViewById(R.id.adminAddDepartureFromInputCity);
        addNewFlightDepartureCity.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        addNewFlightDepartureDateAndTime = findViewById(R.id.adminAddDepartureDateAndTime);
        departureDateAndTimeLabel = findViewById(R.id.adminAddDepartureDateAndTimeLabel);

        addNewFlightForCountry = findViewById(R.id.adminAddFlightForInputCountry);
        addNewFlightForCountry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        addNewFlightForCity = findViewById(R.id.adminAddFlightForInputCity);
        addNewFlightForCity.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        addNewFlightForDateAndTime = findViewById(R.id.adminAddFlightForDateAndTime);
        flightForDateAndTimeLabel = findViewById(R.id.adminAddFlightForDateAndTimeLabel);

        addNewFlightPriceBusinessTicket = findViewById(R.id.adminAddFlightPriceBusinessInput);
        addNewFlightBusinessClassTicketNumber = findViewById(R.id.adminAddFlightNumberBusinessTicketInput);

        addNewFlightPriceFirstClassTicket = findViewById(R.id.adminAddFlightPriceFirstClassInput);
        addNewFlightFirstClassTicketNumber = findViewById(R.id.adminAddFlightNumberFirstClassTicketInput);

        addNewFlightSaveButton = findViewById(R.id.adminAddFlightSaveButton);
        addNewFlightCancelButtom = findViewById(R.id.adminAddFlightCancelButton);

        dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());

        //Creating database
        MyDB = new DBHelper(this);

        //Creating table in database
        MyDB.queryData("CREATE TABLE IF NOT EXISTS flights(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "departureCountry TEXT, departureCity TEXT, departureDateTime DATETIME," +
                "flightForCountry TEXT, flightForCity TEXT, flightForDateTime DATETIME," +
                "businessTicket DOUBLE, firstClassTicket DOUBLE)");

        //Add new user to SQLite
        addNewFlightSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String departureCountry = addNewFlightDepartureCountry.getText().toString();
                String departureCity = addNewFlightDepartureCity.getText().toString();
                String departureDateTime = departureDateAndTimeLabel.getText().toString();
                String flightForCountry = addNewFlightForCountry.getText().toString();
                String flightForCity = addNewFlightForCity.getText().toString();
                String flightForDateTime = flightForDateAndTimeLabel.getText().toString();
                String businessTicketNumber = addNewFlightBusinessClassTicketNumber.getText().toString();
                String firstClassTicketNumber = addNewFlightFirstClassTicketNumber.getText().toString();
                Double businessTicket = null;
                Double firstClassTicket = null;
                try {
                    businessTicket = Double.parseDouble(addNewFlightPriceBusinessTicket.getText().toString());
                    firstClassTicket = Double.parseDouble(addNewFlightPriceFirstClassTicket.getText().toString());
                } catch (NumberFormatException emptyString){
                    Toast.makeText(FlightsADD.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }

                if(departureCountry.equals("") || departureCity.equals("") || flightForCountry.equals("") || flightForCity.equals("") || addNewFlightPriceBusinessTicket.getText().toString().equals("") ||
                        addNewFlightPriceFirstClassTicket.getText().toString().equals("") || departureDateTime.equals("Date and time departure") || flightForDateTime.equals("Date and time flight for") ||
                        businessTicketNumber.equals("") || firstClassTicketNumber.equals("")){
                    Toast.makeText(FlightsADD.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean insert = MyDB.insertDataFlight(departureCountry, departureCity, departureDateTime, flightForCountry, flightForCity, flightForDateTime, businessTicket, firstClassTicket, Integer.valueOf(businessTicketNumber), Integer.valueOf(firstClassTicketNumber));
                    if (insert == true) {
                        Toast.makeText(FlightsADD.this, "Successful add a new flight", Toast.LENGTH_SHORT).show();
                        addNewFlightDepartureCountry.setText("");
                        addNewFlightDepartureCity.setText("");
                        addNewFlightForCountry.setText("");
                        addNewFlightForCity.setText("");
                        addNewFlightPriceBusinessTicket.setText("");
                        addNewFlightPriceFirstClassTicket.setText("");
                        departureDateAndTimeLabel.setText("Date and time departure");
                        flightForDateAndTimeLabel.setText("Date and time flight for");
                        //Go user to login with new acc
                        Intent intent = new Intent(getApplicationContext(), FlightsCRUD.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(FlightsADD.this, "Add a new flight failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Cancel button -> Go back to show all users
        addNewFlightCancelButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlightsCRUD.class);
                startActivity(intent);
                finish();
            }
        });

        addNewFlightDepartureDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                clickedDepartureDateTime = true;
                clickedFlightForeDateTime = false;
                DatePickerDialog datePickerDialog = new DatePickerDialog(FlightsADD.this, mDateDataSet, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        addNewFlightForDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                clickedFlightForeDateTime = true;
                clickedDepartureDateTime = false;
                DatePickerDialog datePickerDialog = new DatePickerDialog(FlightsADD.this, mDateDataSet, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }

    /* After user decided on a date, store those in our calendar variable and then start the TimePickerDialog immediately */
    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(FlightsADD.this, mTimeDataSet, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        }
    };

    /* After user decided on a time, save them into our calendar instance, and now parse what our calendar has into the TextView */
    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            if(clickedDepartureDateTime == true){
                departureDateAndTimeLabel.setText(dateTimeFormat.format(calendar.getTime()));
            }
            else if(clickedFlightForeDateTime == true){
                flightForDateAndTimeLabel.setText(dateTimeFormat.format(calendar.getTime()));
            }
            clickedDepartureDateTime = false;
            clickedFlightForeDateTime = false;
        }
    };

    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), FlightsCRUD.class);
        startActivity(intent);
        finish();
    }
}