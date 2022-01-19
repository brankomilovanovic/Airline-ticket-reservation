package com.example.flights;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.admins.AdminsUserEDIT;
import com.example.admins.AdminsUsersCRUD;
import com.example.singiair.DBHelper;
import com.example.singiair.R;
import com.example.singiair.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FlightsEDIT extends AppCompatActivity {

    EditText editFlightDepartureCountry, editFlightDepartureCity, editFlightForCountry, editFlightForCity, editFlightPriceBusinessTicket,
            editFlightPriceFirstClassTicket, editFlightFirstClassTicketNumber, editFlightBusinessClassTicketNumber;
    Button editFlightDepartureDateAndTime, editFlightForDateAndTime, editFlightSaveButton, editFlightCancelButtom;
    TextView editDepartureDateAndTimeLabel, editFlightForDateAndTimeLabel, editFlightLabel, editFlightLabelSubmit;

    public static DBHelper MyDB;

    private SimpleDateFormat dateTimeFormat;
    private Calendar calendar;

    //check which button is pressed
    boolean clickedDepartureDateTime = false;
    boolean clickedFlightForeDateTime = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights_add);

        Intent intent = getIntent();
        Flights flights = (Flights) intent.getSerializableExtra("flights");

        editFlightDepartureCountry = findViewById(R.id.adminAddDepartureFromInputCountry);
        editFlightDepartureCountry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        editFlightDepartureCity = findViewById(R.id.adminAddDepartureFromInputCity);
        editFlightDepartureCity.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        editFlightDepartureDateAndTime = findViewById(R.id.adminAddDepartureDateAndTime);
        editDepartureDateAndTimeLabel = findViewById(R.id.adminAddDepartureDateAndTimeLabel);

        editFlightForCountry = findViewById(R.id.adminAddFlightForInputCountry);
        editFlightForCountry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        editFlightForCity = findViewById(R.id.adminAddFlightForInputCity);
        editFlightForCity.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        editFlightForDateAndTime = findViewById(R.id.adminAddFlightForDateAndTime);
        editFlightForDateAndTimeLabel = findViewById(R.id.adminAddFlightForDateAndTimeLabel);

        editFlightPriceBusinessTicket = findViewById(R.id.adminAddFlightPriceBusinessInput);
        editFlightBusinessClassTicketNumber = findViewById(R.id.adminAddFlightNumberBusinessTicketInput);

        editFlightPriceFirstClassTicket = findViewById(R.id.adminAddFlightPriceFirstClassInput);
        editFlightFirstClassTicketNumber = findViewById(R.id.adminAddFlightNumberFirstClassTicketInput);

        editFlightSaveButton = findViewById(R.id.adminAddFlightSaveButton);
        editFlightCancelButtom = findViewById(R.id.adminAddFlightCancelButton);

        editFlightLabel = findViewById(R.id.adminAddFlightLabel);
        editFlightLabel.setText("Edit flight: " + flights.getFlightDepartureCountry() + " - " + flights.getFlightForCountry());

        editFlightLabelSubmit = findViewById(R.id.adminAddFlightSaveButton);
        editFlightLabelSubmit.setText("Edit flight");

        editFlightDepartureCountry.setText(flights.getFlightDepartureCountry());
        editFlightDepartureCity.setText(flights.getFlightDepartureCity());
        editDepartureDateAndTimeLabel.setText(flights.getFlightDepartureDateAndTime());
        editFlightForCountry.setText(flights.getFlightForCountry());
        editFlightForCity.setText(flights.getFlightForCity());
        editFlightForDateAndTimeLabel.setText(flights.getFlightForDateAndTime());
        editFlightPriceBusinessTicket.setText(String.valueOf(flights.getFlightBusinessTicket()));
        editFlightPriceFirstClassTicket.setText(String.valueOf(flights.getFlightFirstClassTicket()));
        editFlightBusinessClassTicketNumber.setText(String.valueOf(flights.getFlightBusinessTicketNumber()));
        editFlightFirstClassTicketNumber.setText(String.valueOf(flights.getFlightFirstClassTicketNumber()));

        dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault());

        //Instance database
        MyDB = new DBHelper(this);

        editFlightSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editFlightDepartureCountry.getText().toString().equals("") || editFlightDepartureCity.getText().toString().equals("") || editFlightForCountry.getText().toString().equals("") ||
                        editFlightForCity.getText().toString().equals("") || editFlightPriceBusinessTicket.getText().toString().equals("") || editFlightPriceFirstClassTicket.getText().toString().equals("") ||
                        editDepartureDateAndTimeLabel.getText().toString().equals("Date and time departure") || editFlightForDateAndTimeLabel.getText().toString().equals("Date and time flight for") ||
                        editFlightBusinessClassTicketNumber.getText().toString().equals("") || editFlightFirstClassTicketNumber.getText().toString().equals("")) {
                        Toast.makeText(FlightsEDIT.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    String departureCountry = editFlightDepartureCountry.getText().toString();
                    String departureCity = editFlightDepartureCity.getText().toString();
                    String departureDateTime = editDepartureDateAndTimeLabel.getText().toString();
                    String flightForCountry = editFlightForCountry.getText().toString();
                    String flightForCity = editFlightForCity.getText().toString();
                    String flightForDateTime = editFlightForDateAndTimeLabel.getText().toString();
                    Double businessTicket = Double.valueOf(editFlightPriceBusinessTicket.getText().toString());
                    Double firstClassTicket = Double.valueOf(editFlightPriceFirstClassTicket.getText().toString());
                    int businessTicketNumber = Integer.valueOf(editFlightBusinessClassTicketNumber.getText().toString());
                    int firstClassTicketNumber = Integer.valueOf(editFlightFirstClassTicketNumber.getText().toString());

                    flights.setFlightDepartureCountry(departureCountry);
                    flights.setFlightDepartureCity(departureCity);
                    flights.setFlightDepartureDateAndTime(departureDateTime);
                    flights.setFlightForCountry(flightForCountry);
                    flights.setFlightForCity(flightForCity);
                    flights.setFlightForDateAndTime(flightForDateTime);
                    flights.setFlightBusinessTicket(businessTicket);
                    flights.setFlightFirstClassTicket(firstClassTicket);
                    flights.setFlightBusinessTicketNumber(businessTicketNumber);
                    flights.setFlightFirstClassTicketNumber(firstClassTicketNumber);

                    MyDB.updateFlightInfo(flights.get_id(), flights.getFlightDepartureCountry(), flights.getFlightDepartureCity(), flights.getFlightDepartureDateAndTime(), flights.getFlightForCountry(), flights.getFlightForCity(), flights.getFlightForDateAndTime(), flights.getFlightBusinessTicket(), flights.getFlightFirstClassTicket(), flights.getFlightBusinessTicketNumber(), flights.getFlightFirstClassTicketNumber());
                    Toast.makeText(FlightsEDIT.this, "Flights update successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FlightsCRUD.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        editFlightCancelButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FlightsCRUD.class);
                startActivity(intent);
                finish();
            }
        });

        editFlightDepartureDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                clickedDepartureDateTime = true;
                clickedFlightForeDateTime = false;
                new DatePickerDialog(FlightsEDIT.this, mDateDataSet, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editFlightForDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                clickedFlightForeDateTime = true;
                clickedDepartureDateTime = false;
                new DatePickerDialog(FlightsEDIT.this, mDateDataSet, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
            new TimePickerDialog(FlightsEDIT.this, mTimeDataSet, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        }
    };

    /* After user decided on a time, save them into our calendar instance, and now parse what our calendar has into the TextView */
    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            if(clickedDepartureDateTime == true){
                editDepartureDateAndTimeLabel.setText(dateTimeFormat.format(calendar.getTime()));
            }
            else if(clickedFlightForeDateTime == true){
                editFlightForDateAndTimeLabel.setText(dateTimeFormat.format(calendar.getTime()));
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
