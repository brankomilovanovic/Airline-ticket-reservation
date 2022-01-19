package com.example.singiair;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profile.ProfileActivity;
import com.example.reservation.TicketA;
import com.example.tickets.ListTicketReservationActivity;
import com.example.tickets.SearchFlightFragment;
import com.example.tickets.TicketReservation;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Context context;
    private int User_id;
    User user;

    TextView home_activity_name_and_surname, home_activity_money;
    ImageView home_activity_profile_image, home_activity_profile_button, home_activity_reserved_tickets_button, home_activity_check_in_tickets_button, home_activity_payment_button, home_activity_search_flight_button;

    private List<TicketReservation> allTicketReservationList = new ArrayList<>();
    private List<TicketReservation> uniqueTicketReservationList = new ArrayList<>();
    private List<TicketReservation> uniqueCheckInTicket = new ArrayList<>();

    public HomeFragment(Context context, int user_id){
        this.context=context;
        this.User_id=user_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //Instance database
        DBHelper DB = new DBHelper(context);
        user = DB.getUserData(User_id);

        home_activity_name_and_surname = (TextView) rootView.findViewById(R.id.home_activity_name_and_surname);
        home_activity_name_and_surname.setText(user.getUserName() + " " + user.getUserSurname());

        home_activity_money = (TextView) rootView.findViewById(R.id.home_activity_money);
        home_activity_money.setText("$" + String.format("%.2f", user.getUserMoney()));

        home_activity_profile_image = (ImageView) rootView.findViewById(R.id.home_activity_profile_image);
        //Set user image, or if has not image do not change
        try {
            Bitmap image = DB.getImage(user.getUserId());
            if(image.getByteCount() > 0){
                home_activity_profile_image.setImageBitmap(DB.getImage(user.getUserId()));
            } else {
                System.out.println("ERROR! User have not image!");
            }
        } catch (NullPointerException e){
            System.out.println("ERROR! User have not image!");
        }

        home_activity_profile_button = (ImageView) rootView.findViewById(R.id.home_activity_profile_button);
        home_activity_reserved_tickets_button = (ImageView) rootView.findViewById(R.id.home_activity_reserved_tickets_button);
        home_activity_check_in_tickets_button = (ImageView) rootView.findViewById(R.id.home_activity_check_in_tickets_button);
        home_activity_payment_button = (ImageView) rootView.findViewById(R.id.home_activity_payment_button);
        home_activity_search_flight_button = (ImageView) rootView.findViewById(R.id.home_activity_search_flight_button);

        updateAllTicketReservationList();

        home_activity_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                user = DB.getUserData(user.getUserId());
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        home_activity_reserved_tickets_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllTicketReservationList();
                if(uniqueTicketReservationList.size() == 0){
                    Toast.makeText(context, "You have no flight reserved.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, ListTicketReservationActivity.class);
                    user = DB.getUserData(user.getUserId());
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });

        home_activity_check_in_tickets_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllTicketReservationList();
                if(uniqueCheckInTicket.size() == 0){
                    Toast.makeText(context, "You have no flight ticket check in.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(context, TicketA.class);
                    user = DB.getUserData(user.getUserId());
                    intent.putExtra("user", user);
                    intent.putExtra("current_position_in_list", 0);
                    startActivity(intent);
                }
            }
        });

        home_activity_payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity homeActivity = new HomeActivity();
                CharSequence[] items = {"Credit card", "Payment slip"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Payment");
                dialog.setIcon(R.drawable.list_icon);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            final EditText taskEditText = new EditText(context);
                            AlertDialog.Builder dialogPayment = new AlertDialog.Builder(context, user.getUserId());
                            dialogPayment.setTitle("Payment - Credit card");
                            dialogPayment.setIcon(R.drawable.money_dollar);
                            dialogPayment.setMessage("Enter the amount you want to pay:");
                            taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            dialogPayment.setView(taskEditText);
                            dialogPayment.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String money = String.valueOf(taskEditText.getText());
                                    if(money.equals("") || money.equals("0")){
                                        Toast.makeText(context, "Error!\nYou did not enter a ticket number!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        DB.updateUserMoney(user.getUserId(), (user.getUserMoney() + Double.valueOf(money)));
                                        Toast.makeText(context, "You have successfully paid $" + money + " on account.", Toast.LENGTH_LONG).show();
                                        home_activity_money.setText("$" + String.format("%.2f", (user.getUserMoney() + Double.valueOf(money))));
                                        ((HomeActivity)getActivity()).refreshMoneyInHeader(user.getUserMoney() + Double.valueOf(money));

                                    }
                                }
                            });

                            dialogPayment.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialogPayment.show();
                        }
                        if (which == 1){
                            final EditText taskEditText = new EditText(context);
                            AlertDialog.Builder dialogPayment = new AlertDialog.Builder(context, user.getUserId());
                            dialogPayment.setTitle("Payment - Payment slip");
                            dialogPayment.setIcon(R.drawable.money_dollar);
                            dialogPayment.setMessage("Enter the amount you want to pay:");
                            taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            dialogPayment.setView(taskEditText);
                            dialogPayment.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String money = String.valueOf(taskEditText.getText());
                                    if(money.equals("") || money.equals("0")){
                                        Toast.makeText(context, "Error!\nYou did not enter a number!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Use the Builder class for convenient dialog construction
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("Payer:" + "\n" +
                                                user.getUserName() + " " + user.getUserSurname() + "\n" +"Your address" + "\n\n" +
                                                "Purpose of payment:" + "\n" +
                                                "Payment of money to the account" + "\n\n" +
                                                "Recipient:" + "\n" + "SingiAir d.o.o Belgrade" + "\n\n" +
                                                "Amount:" + "\n" + money + "\n\n" +
                                                "Recipient's account:" + "\n" + "192-3241123311-22" + "\n\n" +
                                                "Reference number (REQUIRED): " + user.getUserId())
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builder.show();
                                    }
                                }
                            });

                            dialogPayment.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialogPayment.show();
                        }
                    }
                });
                dialog.show();
            }
        });

        home_activity_search_flight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container,
                        new SearchFlightFragment(context, user.getUserId())).commit();
            }
        });
        return rootView;
    }

    private void updateAllTicketReservationList() {
        DBHelper MyDB = new DBHelper(context);
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
        getSelectedCheckInTicket();
    }

    private void getSelectedReservation() {
        uniqueTicketReservationList.clear();
        for (int i = 0; i < allTicketReservationList.size(); i++) {
            if (allTicketReservationList.get(i).getTicketReservationIDUser() == user.getUserId() && allTicketReservationList.get(i).getTicketReservationCheckIn() == false) { // Get equals departure country and city
                uniqueTicketReservationList.add(allTicketReservationList.get(i));
            }
        }
    }

    private void getSelectedCheckInTicket() {
        uniqueCheckInTicket.clear();
        for (int i = 0; i < allTicketReservationList.size(); i++) {
            if ((allTicketReservationList.get(i).getTicketReservationIDUser() == user.getUserId()) && (allTicketReservationList.get(i).getTicketReservationCheckIn() == true)) { // Get equals departure country and city
                uniqueCheckInTicket.add(allTicketReservationList.get(i));
            }
        }
    }
}