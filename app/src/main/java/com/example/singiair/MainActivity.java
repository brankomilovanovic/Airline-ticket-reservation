package com.example.singiair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

// TODO: SREDITI MALO AKTIVIRANJE ADMIN KODA I ADMIN MENU PREBACITI U ADMINS I TAMO DODATI (Context context)
// TODO: URADITI KADA KORISNIK SAM SEBI SKINE ADMINA DA IZBACI SA CRUD LISTE I POSALJE NA HOME I TAMO DA NEMA AKTIVIRAN ADMIN NEGO DA STOJI ADMIN CODE ACTIVATE
// TODO: IZBACUJE GRESKU KADA SE UZME PREVISE VELIKA SLIKA NE MOZE DA JE OBRADI, STAVITI IZUZETAK
// FIXME: KADA ADMIN OBRISE NEKOG KORISNIKA PRVI ISPOD NJEGA DOBIJA NJEGOVU SLIKU SAMO U PRVOM PRIKAZU
// TODO: NAPRAVITI KADA KORISNIK DODAJE NOVI LET DA NE MOZE BIRATI DATUM KOJI JE VREC PROSAO
// TODO: NAPRAVITI PROVERU KADA KORISNIK DODAJE DATUM I VREME DOLASKA LETA NE MOZE IZABRATI MANJI DATUM I VREME OD DATUMA KADA JE KRENUO
// TODO: NAPRAVITI PROVERU SVAKI MINUT DA LI JE PROSLO NEKO VREME POLETANJA, AKO JE PROSLO VREME POLETANJA SETOVATI U FAJLU POLETEO NA TRUE NAKON TOGA PROVERITI SVAKO VREME SLETANJA AKO JE LOKALNO VREME PROSLO OD VREMENA SLETANJA OBRISATI LET
// TODO: URADITI KADA SE SA POCETNE STRANE HOME ACTIVITY PRETRAZUJE LETOVI DA SE TAJ FRAGMENT UGASI A KADA HOCE SA DRUGOG FRAGMENTA NAZAD ONBACKPRESS DA POSALJE VREDNOSTI I VRATI GA NA HOME
// FIXME: KADA SE DODA NOVAC NA RACUN PREKO FRAGMENT HOME NE UCITA NA HEADERU

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get database
        DBHelper DB = new DBHelper(this);

        //We supply the user if he is already logged in
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String userIsLoggedUsername = sharedPreferences.getString("username", null);
        String userIsLoggedPassword = sharedPreferences.getString("password", null);
        boolean userIsLoggedIn = sharedPreferences.getBoolean("is_logged", false);

        if(userIsLoggedIn == true){
            //Set logged user info
            User user = DB.setLoginUserData(userIsLoggedUsername, userIsLoggedPassword);

            //Start home activity with user info
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
            return;
        } else {
            //If user isn't logged
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}