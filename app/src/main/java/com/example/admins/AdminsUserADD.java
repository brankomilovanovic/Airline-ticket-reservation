package com.example.admins;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.profile.EditProfileActivity;
import com.example.singiair.DBHelper;
import com.example.singiair.R;
import com.example.singiair.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class AdminsUserADD extends AppCompatActivity {

    EditText addNewUserName, addNewUserSurname, addNewUserUsername, addNewUserEmail, addNewUserPassword, addNewUserMoney;
    Switch addNewUserPosition;
    Button addNewUserSaveButton, addNewUserCancelButton;
    ImageView addNewUserImage;
    User userLogged;

    final int REQUEST_CODE_GALLERY = 999;

    public static DBHelper MyDB;

    String setUserPosition = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_user_add);

        Intent intent = getIntent();
        userLogged = (User) intent.getSerializableExtra("userLogged");

        addNewUserName = findViewById(R.id.adminUserProfileNameInput);
        addNewUserName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        addNewUserSurname = findViewById(R.id.adminUserProfileSurnameInput);
        addNewUserSurname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        addNewUserUsername = findViewById(R.id.adminUserProfileUsernameInput);
        addNewUserEmail = findViewById(R.id.adminUserProfileEmailInput);
        addNewUserPassword = findViewById(R.id.adminUserProfilePasswordInput);
        addNewUserPosition = (Switch) findViewById(R.id.adminUserProfileSetPositionSwitch);
        addNewUserImage = (ImageView) findViewById(R.id.adminUserProfilePhotoInput);
        addNewUserMoney = findViewById(R.id.adminUserProfileMoneyInput);

        addNewUserSaveButton = findViewById(R.id.adminUserProfileSaveButton);
        addNewUserCancelButton = findViewById(R.id.adminUserProfileCancelButton);

        //Creating database
        MyDB = new DBHelper(this);

        //Creating table in database
        MyDB.queryData("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR," +
                "surname VARCHAR, username VARCHAR, email VARCHAR, password VARCHAR, position VARCHAR, image BLOB)");

        //Select image by on imageview click
        addNewUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Read external storage permission to select image from gallery
                ActivityCompat.requestPermissions(
                        AdminsUserADD.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY
                );
            }
        });

        addNewUserPosition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setUserPosition = "admin";
                } else {
                    setUserPosition = "user";
                }
            }
        });

        //Add new user to SQLite
        addNewUserSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = addNewUserName.getText().toString();
                String userSurname = addNewUserSurname.getText().toString();
                String userUsername = addNewUserUsername.getText().toString();
                String userEmail = addNewUserEmail.getText().toString();
                String userPassword = addNewUserPassword.getText().toString();
                double userMoney = Double.parseDouble(addNewUserMoney.getText().toString());

                if(userName.equals("") || userSurname.equals("") || userUsername.equals("") || userEmail.equals("") || userPassword.equals("") || addNewUserMoney.getText().toString().equals("")){
                    Toast.makeText(AdminsUserADD.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    Toast.makeText(AdminsUserADD.this, "E-mail address not in the correct format", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Check if that user already exists in the database
                    Boolean checkUser = MyDB.checkUsername(userUsername);
                    if (checkUser == false) {
                        userName = userName.substring(0,1).toUpperCase() + userName.substring(1).toLowerCase();
                        userSurname = userSurname.substring(0,1).toUpperCase() + userSurname.substring(1).toLowerCase();

                        Boolean insert = MyDB.insertData(userName, userSurname, userUsername, userEmail, userPassword, setUserPosition, imageViewToByte(addNewUserImage), false, userMoney);
                        if (insert == true) {
                            Toast.makeText(AdminsUserADD.this, "Create new user successful", Toast.LENGTH_SHORT).show();
                            addNewUserName.setText("");
                            addNewUserSurname.setText("");
                            addNewUserUsername.setText("");
                            addNewUserEmail.setText("");
                            addNewUserPassword.setText("");
                            addNewUserImage.setImageResource(R.drawable.add_user_photo);
                            addNewUserMoney.setText("0");
                            //Go user to login with new acc
                            Intent intent = new Intent(getApplicationContext(), AdminsUsersCRUD.class);
                            intent.putExtra("userLogged", userLogged);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AdminsUserADD.this, "Create new user failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AdminsUserADD.this, "User already exist with this username", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Cancel button -> Go back to show all users
        addNewUserCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminsUsersCRUD.class);
                intent.putExtra("userLogged", userLogged);
                startActivity(intent);
                finish();
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
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
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
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
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
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
                addNewUserImage.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed(){
        Intent intent = new Intent(AdminsUserADD.this, AdminsUsersCRUD.class);
        intent.putExtra("userLogged", userLogged);
        startActivity(intent);
        finish();
    }
}