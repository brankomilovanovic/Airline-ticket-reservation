package com.example.singiair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admins.AdminsUserADD;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button buttonConfirmLogin, buttonDontHaveAcc;
    DBHelper DB = new DBHelper(this);
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Input information for login
        loginUsername = (EditText) findViewById(R.id.loginInputUsername);
        loginPassword = (EditText) findViewById(R.id.loginInputPassword);

        //Button in login form
        buttonConfirmLogin = (Button) findViewById(R.id.loginConfirmButton);
        buttonDontHaveAcc = (Button) findViewById(R.id.loginDontHaveAccButton);

        //Instance database
        DB = new DBHelper(getApplicationContext());

        //Shared preferences
        sharedpreferences = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        buttonConfirmLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userUsername = loginUsername.getText().toString();
                String userPassword = loginPassword.getText().toString();

                if(userUsername.equals("") || userPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUserAndPassword = DB.checkUsernameAndPassword(userUsername, userPassword);
                    if(checkUserAndPassword == true){
                        User user = DB.setLoginUserData(userUsername, userPassword);
                        DB.updateUserIsLogged(user.getUserId(), true); // Set user is_logged to true
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        //Go to home activity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();

                        //Set users preferences for again start app
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("username", user.getUserUsername());
                        editor.putString("password", user.getUserPassword());
                        editor.putBoolean("is_logged", true);
                        editor.commit();

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonDontHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}