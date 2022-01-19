package com.example.profile;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admins.AdminsUserADD;
import com.example.admins.AdminsUserEDIT;
import com.example.reservation.TicketA;
import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.LoginActivity;
import com.example.singiair.R;
import com.example.singiair.RegistrationActivity;
import com.example.singiair.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {

    EditText editNameInput, editSurnameInput, editEmailInput, editUsernameInput;
    Button editSaveButton, editCancelButton;
    ImageView editProfileImageInput;
    DBHelper DB;
    User user;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        //Instance databases
        DBHelper DB = new DBHelper(EditProfileActivity.this);

        editNameInput = (EditText) findViewById(R.id.editProfileNameInput);
        editNameInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        editSurnameInput = (EditText) findViewById(R.id.editProfileSurnameInput);
        editSurnameInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        editEmailInput = (EditText) findViewById(R.id.editProfileEmailInput);
        editUsernameInput = (EditText) findViewById(R.id.editProfileUsernameInput);
        editProfileImageInput = findViewById(R.id.editProfileImage);

        editSaveButton = (Button) findViewById(R.id.editProfileButtonSave);
        editCancelButton = (Button) findViewById(R.id.editProfileCancelButton);

        editNameInput.setText(user.getUserName());
        editSurnameInput.setText(user.getUserSurname());
        editEmailInput.setText(user.getUserEmail());
        editUsernameInput.setText(user.getUserUsername());

        //Set user image, or if has not image do not change
        try {
            Bitmap image = DB.getImage(user.getUserId());
            if(image.getByteCount() > 0){
                editProfileImageInput.setImageBitmap(DB.getImage(user.getUserId()));
            } else {
                System.out.println("ERROR! User have not image!");
            }
        } catch (NullPointerException e){
            System.out.println("ERROR! User have not image!");
        }

        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editNameInput.getText().toString().equals("") || editSurnameInput.getText().toString().equals("") || editUsernameInput.getText().toString().equals("") || editEmailInput.getText().toString().equals("")){
                    Toast.makeText(EditProfileActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(editEmailInput.getText().toString()).matches()) {
                    Toast.makeText(EditProfileActivity.this, "E-mail address not in the correct format", Toast.LENGTH_SHORT).show();
                } else {
                    String userName = editNameInput.getText().toString();
                    String userSurname = editSurnameInput.getText().toString();
                    String userUsername = editUsernameInput.getText().toString();
                    String email = editEmailInput.getText().toString();

                    boolean checkUsernameExist = DB.checkUsername(userUsername);
                    if((!userUsername.equals(user.getUserUsername())) && checkUsernameExist == true){
                        Toast.makeText(EditProfileActivity.this, "User already exist with this username", Toast.LENGTH_SHORT).show();
                    } else {
                        userName = userName.substring(0,1).toUpperCase() + userName.substring(1).toLowerCase();
                        userSurname = userSurname.substring(0,1).toUpperCase() + userSurname.substring(1).toLowerCase();

                        Toast.makeText(EditProfileActivity.this, "Successfully update profile", Toast.LENGTH_SHORT).show();

                        DB.updateUserInfo(user.getUserId(), userUsername, userName, userSurname, email, user.getUserPassword(), user.getUserPosition(), imageViewToByte(editProfileImageInput), user.getIsLogged(), user.getUserMoney());

                        user.setUserName(userName);
                        user.setUserSurname(userSurname);
                        user.setUserUsername(userUsername);
                        user.setUserEmail(email);

                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        editCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        //Select image by on imageview click
        editProfileImageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Read external storage permission to select image from gallery
                ActivityCompat.requestPermissions(
                        EditProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY
                );
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
                editProfileImageInput.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed(){
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}