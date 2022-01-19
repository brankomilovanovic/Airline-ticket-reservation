package com.example.singiair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admins.AdminsUserADD;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class RegistrationActivity extends AppCompatActivity {

    EditText registrationName, registrationSurname, registrationUsername, registrationEmail, registrationPassword;
    Button registrationConfirmButton, registrationExistingUserButton;
    DBHelper DB;
    ImageView registrationImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Input information for registration new user
        registrationName = (EditText) findViewById(R.id.registrationInputName);
        registrationName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        registrationSurname = (EditText) findViewById(R.id.registrationInputSurname);
        registrationSurname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        registrationUsername = (EditText) findViewById(R.id.registrationInputUsername);
        registrationEmail = (EditText) findViewById(R.id.registrationInputEmail);
        registrationPassword = (EditText) findViewById(R.id.registrationInputPassword);
        registrationImage = (ImageView) findViewById(R.id.registrationInputImage);

        //Button in registration form
        registrationConfirmButton = (Button) findViewById(R.id.registrationConfirmButton);
        registrationExistingUserButton = (Button) findViewById(R.id.registrationUserExistingButton);

        //Instance database
        DB = new DBHelper(this);

        //Creating table in database
        DB.queryData("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR," +
                "surname VARCHAR, username VARCHAR, email VARCHAR, password VARCHAR, position VARCHAR, image BLOB)");

        //Select image by on imageview click
        registrationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Read external storage permission to select image from gallery
                ActivityCompat.requestPermissions(
                        RegistrationActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999
                );
            }
        });

        //Registration
        registrationConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = registrationName.getText().toString();
                String userSurname = registrationSurname.getText().toString();
                String userUsername = registrationUsername.getText().toString();
                String userEmail = registrationEmail.getText().toString();
                String userPassword = registrationPassword.getText().toString();

                if(userName.equals("") || userSurname.equals("") || userUsername.equals("") || userEmail.equals("") || userPassword.equals("")){
                    Toast.makeText(RegistrationActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    Toast.makeText(RegistrationActivity.this, "E-mail address not in the correct format", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Check if that user already exists in the database
                    Boolean checkUser = DB.checkUsername(userUsername);
                    if (checkUser == false) {
                        userName = userName.substring(0,1).toUpperCase() + userName.substring(1).toLowerCase();
                        userSurname = userSurname.substring(0,1).toUpperCase() + userSurname.substring(1).toLowerCase();

                        Boolean insert = DB.insertData(userName, userSurname, userUsername, userEmail, userPassword, "user",  imageViewToByteRegistration(registrationImage), false,0);
                        if (insert == true) {
                            Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            registrationName.setText("");
                            registrationSurname.setText("");
                            registrationUsername.setText("");
                            registrationEmail.setText("");
                            registrationPassword.setText("");
                            registrationImage.setImageResource(R.drawable.add_user_photo);
                            //Go user to login with new acc
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "User already exist with this username", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Login
        registrationExistingUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static byte[] imageViewToByteRegistration(ImageView image) {
        try {
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }
        catch (ClassCastException e){
            byte[] byteArray = new byte[0];
            return byteArray;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 999){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 999);
            }
            else {
                Toast.makeText(this, "Don't have permission to access", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 999 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //Set image choosed from gallery to image view
                registrationImage.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        DBHelper MyDB = new DBHelper(this);
        startActivity(intent);
        finish();
    }
}