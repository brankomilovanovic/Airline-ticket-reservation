package com.example.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.R;
import com.example.singiair.User;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, profileSurname, profileEmail, profileUsername, profileBalance;
    Button editProfileButton, changePasswordButton, profileBackButton;
    ImageView profileImage;
    DBHelper DB;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = (TextView) findViewById(R.id.profileNameLabel);
        profileSurname = (TextView) findViewById(R.id.profileSurnameLabel);
        profileEmail = (TextView) findViewById(R.id.profileEmailLabel);
        profileUsername = (TextView) findViewById(R.id.profileUsernameLabel);
        profileBalance = (TextView) findViewById(R.id.profileBalanceLabel);

        editProfileButton = (Button) findViewById(R.id.profileEditButton);
        changePasswordButton = (Button) findViewById(R.id.profileChangePasswordButton);
        profileBackButton = (Button) findViewById(R.id.profileButtonBack);

        profileImage = (ImageView) findViewById(R.id.profileUserImage);

        //Instance database
        DB = new DBHelper(getApplicationContext());

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        //Set label
        profileUsername.setText("Username: " + user.getUserUsername());
        profileName.setText("Name: " + user.getUserName());
        profileEmail.setText("E-mail: " + user.getUserEmail());
        profileSurname.setText("Surname: " + user.getUserSurname());
        profileBalance.setText("Account balance: $" + String.format("%.2f", user.getUserMoney()));

        //Set user image, or if has not image do not change
        try {
            Bitmap image = DB.getImage(user.getUserId());
            if(image.getByteCount() > 0){
                profileImage.setImageBitmap(DB.getImage(user.getUserId()));
            } else {
                System.out.println("ERROR! User have not image!");
            }
        } catch (NullPointerException e){
            System.out.println("ERROR! User have not image!");
        }

        //Back button (go on home activity)
        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfilePasswordActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}