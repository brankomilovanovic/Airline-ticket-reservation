package com.example.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.singiair.DBHelper;
import com.example.singiair.R;
import com.example.singiair.User;

import java.io.ByteArrayOutputStream;

public class EditProfilePasswordActivity extends AppCompatActivity {

    EditText editCurrentPasswordInput, editNewPasswordInput, editRepeatNewPasswordInput;
    ImageView editProfilePasswordImage;
    Button editSavePasswordButton, editCancelPasswordButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_password);

        //Instance databases
        DBHelper DB = new DBHelper(EditProfilePasswordActivity.this);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        editProfilePasswordImage = findViewById(R.id.editoProfilePasswordImage);

        //Set user image, or if has not image do not change
        try {
            Bitmap image = DB.getImage(user.getUserId());
            if(image.getByteCount() > 0){
                editProfilePasswordImage.setImageBitmap(DB.getImage(user.getUserId()));
            } else {
                System.out.println("ERROR! User have not image!");
            }
        } catch (NullPointerException e){
            System.out.println("ERROR! User have not image!");
        }

        editCurrentPasswordInput = (EditText) findViewById(R.id.editProfilePasswordCurrentInput);
        editNewPasswordInput = (EditText) findViewById(R.id.editProfilePasswordNewInput);
        editRepeatNewPasswordInput = (EditText) findViewById(R.id.editProfilePasswordRepeatNewInput);

        editSavePasswordButton = (Button) findViewById(R.id.editProfilePasswordButtonSave);
        editCancelPasswordButton = (Button) findViewById(R.id.editProfilePasswordCancelButton);

        editSavePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCurrentPasswordInput.getText().toString().equals("") || editNewPasswordInput.getText().toString().equals("") || editRepeatNewPasswordInput.getText().toString().equals("")) {
                    Toast.makeText(EditProfilePasswordActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else if (!editCurrentPasswordInput.getText().toString().equals(user.getUserPassword())) {
                    Toast.makeText(EditProfilePasswordActivity.this, "Current password is not correct", Toast.LENGTH_SHORT).show();
                } else if (!editNewPasswordInput.getText().toString().equals(editRepeatNewPasswordInput.getText().toString())) {
                    Toast.makeText(EditProfilePasswordActivity.this, "New password do not match", Toast.LENGTH_SHORT).show();
                } else {
                    String password = editNewPasswordInput.getText().toString();
                    DB.updateUserInfo(user.getUserId(), user.getUserUsername(), user.getUserName(), user.getUserSurname(), user.getUserEmail(), password, user.getUserPosition(), imageViewToByte(editProfilePasswordImage), user.getIsLogged(), user.getUserMoney());
                    Toast.makeText(EditProfilePasswordActivity.this, "Successfully password change", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfilePasswordActivity.this, ProfileActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
        });

        editCancelPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfilePasswordActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
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

    public void onBackPressed(){
        Intent intent = new Intent(EditProfilePasswordActivity.this, ProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}