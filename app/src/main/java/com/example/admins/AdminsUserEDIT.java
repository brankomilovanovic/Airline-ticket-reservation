package com.example.admins;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.profile.EditProfileActivity;
import com.example.profile.ProfileActivity;
import com.example.singiair.DBHelper;
import com.example.singiair.HomeActivity;
import com.example.singiair.LoginActivity;
import com.example.singiair.R;
import com.example.singiair.RegistrationActivity;
import com.example.singiair.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class AdminsUserEDIT extends AppCompatActivity {

    EditText editUserName, editUserSurname, editUserUsername, editUserEmail, editUserPassword, editUserMoney;
    TextView editUserLabel, editUserLogged;
    Switch editUserPosition;
    Button editUserSaveButton, editUserCancelButton;
    ImageView editUserImage;
    User user;
    User userLogged;

    final int REQUEST_CODE_GALLERY = 999;

    public static DBHelper MyDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_user_add);

        Intent intent = getIntent();
        userLogged = (User) intent.getSerializableExtra("userLogged");
        user = (User) intent.getSerializableExtra("user");

        //Instance databases
        DBHelper MyDB = new DBHelper(AdminsUserEDIT.this);

        editUserName = findViewById(R.id.adminUserProfileNameInput);
        editUserName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        editUserSurname = findViewById(R.id.adminUserProfileSurnameInput);
        editUserSurname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); // Set first latter capital
        editUserUsername = findViewById(R.id.adminUserProfileUsernameInput);
        editUserEmail = findViewById(R.id.adminUserProfileEmailInput);
        editUserPassword = findViewById(R.id.adminUserProfilePasswordInput);
        editUserPosition = (Switch) findViewById(R.id.adminUserProfileSetPositionSwitch);
        editUserImage = (ImageView) findViewById(R.id.adminUserProfilePhotoInput);
        editUserLogged = findViewById(R.id.adminUserProfileSetLogged);
        editUserMoney = findViewById(R.id.adminUserProfileMoneyInput);

        editUserSaveButton = findViewById(R.id.adminUserProfileSaveButton);
        editUserCancelButton = findViewById(R.id.adminUserProfileCancelButton);

        editUserLabel = findViewById(R.id.adminUserProfileLabel);
        editUserLabel.setText("Edit user " + user.getUserUsername());

        editUserName.setText(user.getUserName());
        editUserSurname.setText(user.getUserSurname());
        editUserUsername.setText(user.getUserUsername());
        editUserEmail.setText(user.getUserEmail());
        editUserPassword.setText(user.getUserPassword());
        editUserMoney.setText(String.format("%.2f", user.getUserMoney()));

        //Set switch to position YES = admin or NO = user
        if(user.getUserPosition().equals("admin")) { editUserPosition.setChecked(true); } else {  editUserPosition.setChecked(false); }

        //Set switch to position YES = admin or NO = user
        if(user.getIsLogged() == true) { editUserLogged.setText("User LOGGED: YES"); } else {  editUserLogged.setText("User LOGGED: NO"); }

        //Set user image, or if has not image do not change
        try {
            Bitmap image = MyDB.getImage(user.getUserId());
            if(image.getByteCount() > 0){
                editUserImage.setImageBitmap(MyDB.getImage(user.getUserId()));
            } else {
                System.out.println("ERROR! User have not image!");
            }
        } catch (NullPointerException e){
            System.out.println("ERROR! User have not image!");
        }

        editUserSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editUserName.getText().toString().equals("") || editUserSurname.getText().toString().equals("") || editUserUsername.getText().toString().equals("") ||
                        editUserEmail.getText().toString().equals("") || editUserPassword.getText().toString().equals("") || editUserMoney.getText().toString().equals("")){
                    Toast.makeText(AdminsUserEDIT.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUserNameExist = MyDB.checkUsername(editUserUsername.getText().toString());
                    if ((!editUserUsername.getText().toString().equals(user.getUserUsername())) && checkUserNameExist == true) {
                        Toast.makeText(AdminsUserEDIT.this, "User already exist with this username", Toast.LENGTH_SHORT).show();
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(editUserEmail.getText().toString()).matches()) {
                            Toast.makeText(AdminsUserEDIT.this, "E-mail address not in the correct format", Toast.LENGTH_SHORT).show();
                    } else {
                        String editUserNameTitle = editUserName.getText().toString();
                        String editUserSurnameTitle = editUserSurname.getText().toString();
                        editUserNameTitle = editUserNameTitle.substring(0, 1).toUpperCase() + editUserNameTitle.substring(1).toLowerCase();
                        editUserSurnameTitle = editUserSurnameTitle.substring(0, 1).toUpperCase() + editUserSurnameTitle.substring(1).toLowerCase();

                        user.setUserUsername(editUserUsername.getText().toString());
                        user.setUserEmail(editUserEmail.getText().toString());
                        user.setUserPassword(editUserPassword.getText().toString());
                        user.setUserMoney(Double.parseDouble(editUserMoney.getText().toString()));

                        if (editUserPosition.isChecked() == true) { user.setUserPosition("admin"); } else { user.setUserPosition("user"); }

                        MyDB.updateUserInfo(user.getUserId(), user.getUserUsername(), editUserNameTitle, editUserSurnameTitle, user.getUserEmail(), user.getUserPassword(), user.getUserPosition(), imageViewToByte(editUserImage), user.getIsLogged(), user.getUserMoney());
                        Toast.makeText(AdminsUserEDIT.this, "User update successful", Toast.LENGTH_SHORT).show();

                        if(userLogged.getUserId() == user.getUserId()){
                            if(user.getUserPosition().equals("user")){
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), AdminsUsersCRUD.class);
                                intent.putExtra("userLogged", user);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AdminsUsersCRUD.class);
                            intent.putExtra("userLogged", userLogged);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });

        //Select image by on imageview click
        editUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Read external storage permission to select image from gallery
                ActivityCompat.requestPermissions(
                        AdminsUserEDIT.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY
                );
            }
        });


        editUserCancelButton.setOnClickListener(new View.OnClickListener() {
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
                editUserImage.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed(){
        Intent intent = new Intent(AdminsUserEDIT.this, AdminsUsersCRUD.class);
        intent.putExtra("userLogged", userLogged);
        startActivity(intent);
        finish();
    }
}
