package com.example.admins;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.flights.FlightsCRUD;
import com.example.reservation.TicketB;
import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.LoginActivity;
import com.example.singiair.R;
import com.example.singiair.User;

import java.util.ArrayList;
import java.util.Locale;

public class AdminsUsersCRUD extends AppCompatActivity{

    ListView allUsersList;
    ArrayList<User> allUsers;
    ArrayList<User> allUsersFiltered;
    AdminsUsersListAdapter usersAdapter;
    View addNewUser;
    boolean filtered = false;
    User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_users_crud);

        Intent intent = getIntent();
        userLogged = (User) intent.getSerializableExtra("userLogged");

        EditText searchUser = findViewById(R.id.searchUserInput);

        addNewUser = findViewById(R.id.adminCrudAddNewUserButton);
        allUsersList = findViewById(R.id.flightCrudListView);
        allUsers = new ArrayList<>();
        usersAdapter = new AdminsUsersListAdapter(this, R.layout.users_crud, allUsers);
        allUsersList.setAdapter(usersAdapter);

        //get all data from sqlite
        DBHelper MyDB = new DBHelper(AdminsUsersCRUD.this);
        updateAllUsersList();

        if (allUsers.size() == 0){
            //if there is no users in table of databases
            Toast.makeText(this,"No user found", Toast.LENGTH_SHORT).show();
        }

        searchUser.addTextChangedListener(new TextWatcher() {
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

        allUsersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdminsUsersCRUD.this);
                dialog.setTitle("Editing user action");
                dialog.setIcon(R.drawable.list_icon);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //Update user
                            User user;
                            if(filtered == true){ //if use search bar
                                user = MyDB.getUserData(allUsersFiltered.get(position).getUserId());
                            }
                            else {
                                user = MyDB.getUserData(allUsers.get(position).getUserId());
                            }
                            Intent intent = new Intent(AdminsUsersCRUD.this, AdminsUserEDIT.class);
                            intent.putExtra("user", user);
                            intent.putExtra("userLogged", userLogged);
                            startActivity(intent);
                            finish();
                        }
                        if (which == 1){
                            //Delete user
                            if(filtered == true){ //if use search bar
                                showDialogDelete(allUsersFiltered.get(position).getUserId(), allUsersFiltered.get(position).getUserName() + " " + allUsersFiltered.get(position).getUserSurname(), allUsersFiltered.get(position).getUserUsername());
                            }
                            else {
                                showDialogDelete(allUsers.get(position).getUserId(), allUsers.get(position).getUserName() + " " + allUsers.get(position).getUserSurname(), allUsers.get(position).getUserUsername());
                            }
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminsUserADD.class);
                intent.putExtra("userLogged", userLogged);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDialogDelete(int id, String nameAndSurname, String username) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AdminsUsersCRUD.this);
        dialog.setTitle("Delete user!");
        dialog.setMessage("Are you sure to delete?\n\nName: " + nameAndSurname + "\nUsername: " + username + "\nID: " + id);
        DBHelper MyDB = new DBHelper(AdminsUsersCRUD.this);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    if(userLogged.getUserId() == id){
                        SharedPreferences pref = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    MyDB.deleteUser(id);
                    Toast.makeText(AdminsUsersCRUD.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                    if(filtered == true){
                        Intent intent = new Intent(getApplicationContext(), AdminsUsersCRUD.class);
                        intent.putExtra("userLogged", userLogged);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateAllUsersList();
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


    private void updateAllUsersList(){
        //get all user from sqlite
        DBHelper MyDB = new DBHelper(AdminsUsersCRUD.this);
        Cursor cursor = MyDB.getData("SELECT * FROM users");
        allUsers.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String surname = cursor.getString(2);
            String username = cursor.getString(3);
            String email = cursor.getString(4);
            String password = cursor.getString(5);
            String position = cursor.getString(6);
            byte[] image = cursor.getBlob(7);
            boolean is_logged = cursor.getInt(8) > 0;
            double money = cursor.getDouble(9);
            //add to list
            allUsers.add(new User(id, name, surname, email, password, username, position, image, is_logged, money));
        }
        usersAdapter.notifyDataSetChanged();
    }

    private void filter(String text){
        ArrayList<User> filterList = new ArrayList<>();
        for (User user: allUsers){
            String search_user_name_surname = user.getUserName() + " " + user.getUserSurname();
            String search_user_surname_name = user.getUserSurname() + " " + user.getUserName();
            if(user.getUserName().toLowerCase().contains(text.toLowerCase())){ // Get user just with name
                filterList.add(user);
            }
            else if (search_user_name_surname.toLowerCase().contains(text.toLowerCase())) { // Get user with name and surname
                filterList.add(user);
            }
            else if (user.getUserSurname().toLowerCase().contains(text.toLowerCase())){  // Get user just with surname
                filterList.add(user);
            }
            else if (search_user_surname_name.toLowerCase().contains(text.toLowerCase())) { // Get user with surname and name
                filterList.add(user);
            }
            else if (user.getUserUsername().toLowerCase().contains(text.toLowerCase())){ // Get user just with username
                filterList.add(user);
            }
            usersAdapter.filteredList(filterList);
            filtered = true;
            allUsersFiltered = filterList;
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        DBHelper MyDB = new DBHelper(this);
        userLogged = MyDB.getUserData(userLogged.getUserId());
        intent.putExtra("user", userLogged);
        startActivity(intent);
        finish();
    }

}