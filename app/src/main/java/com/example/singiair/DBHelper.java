package com.example.singiair;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.flights.Flights;
import com.example.tickets.TicketReservation;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "SingiAir.db";

    private static final String TABLE_USER = "users";

    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_SURNAME = "surname";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_POSITION = "position";
    private static final String COLUMN_USER_LOGGED = "is_logged";
    private static final String COLUMN_USER_MONEY = "money";
    private User user;

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, surname TEXT, username TEXT, email TEXT, password TEXT, position TEXT, image BLOB, is_logged BOOLEAN, money DOUBLE)");
        MyDB.execSQL("create Table flights(id INTEGER PRIMARY KEY AUTOINCREMENT, departureCountry TEXT, departureCity TEXT, departureDateTime DATETIME, flightForCountry TEXT, flightForCity TEXT, flightForDateTime DATETIME, businessTicket DOUBLE, firstClassTicket DOUBLE, businessTicketNumber INTEGER, firstClassTicketNumber INTEGER)");
        MyDB.execSQL("create Table ticketsReserved(id INTEGER PRIMARY KEY AUTOINCREMENT, id_user INTEGER, id_flight INTEGER, ticket_class TEXT, departureFrom TEXT, flightFor TEXT, dateAndTimeDeparture DATETIME, check_in_user_name TEXT, check_in_user_surname TEXT, check_in_user_passport_number TEXT, check_in_user_nationality TEXT, checkIn BOOLEAN, ticketPrice DOUBLE)");

        //Set some default user
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (1,'Branko', 'Milovanovic', 'bane', 'branko.milovanovic.19@singimail.rs', 'bane', 'admin', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (2,'Marko', 'Milicevic', 'marko', 'marko.milicevic.19@singimail.rs', 'marko', 'user', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (3,'Milan', 'Milanovic', 'milan', 'milan.milanovic.19@singimail.rs', 'milan', 'user', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (4,'Marko', 'Bojkovic', 'markoBojkovic', 'marko.bojkovic.19@singimail.rs', 'marko', 'user', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (5,'Milan', 'Bogicevic', 'milan00', 'milan.bogicevic.19@singimail.rs', 'milan', 'user', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (6,'Milan', 'Ilic', 'milan_ilic', 'milan.ilic.19@singimail.rs', 'milan', 'user', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (7,'Petar', 'Petrovic', 'petar', 'petar.petrovic.19@singimail.rs', 'petar', 'user', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (8,'Milan', 'Stefanovic', 'milance', 'milan.stefanovic.19@singimail.rs', 'milan', 'user', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (9,'Marko', 'Veljkovic', 'marko123', 'marko.veljkovic.19@singimail.rs', 'marko', 'user', null, false, '0')");
        MyDB.execSQL("Insert into users(id,name,surname,username,email,password,position,image,is_logged,money) VALUES (10,'Petar', 'Peric', 'petar_peric', 'petar.peric.19@singimail.rs', 'petar', 'user', null, false, '0')");

        //Set some default flight
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (1,'Srbija', 'Beograd', '01/23/2022 5:00 PM', 'Nemacka', 'Stuttgart', '01/23/2022 6:45 PM', '49.99', '90.00', 20, 10)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (8,'Nemacka', 'Stuttgart', '02/02/2022 3:00 PM', 'Srbija', 'Beograd', '02/02/2022 4:30 PM', '49.99', '119.99', 25, 5)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (2,'Srbija', 'Beograd', '02/02/2022 4:00 PM', 'Nemacka', 'Berlin', '02/02/2022 6:45 PM', '39.99', '79.99', 30, 10)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (9,'Nemacka', 'Berlin', '02/10/2022 7:00 AM', 'Srbija', 'Beograd', '02/10/2022 8:30 PM', '49.99', '119.99', 40, 12)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (3,'Srbija', 'Beograd', '01/22/2022 11:00 AM', 'Francuska', 'Pariz', '01/22/2022 1:30 PM', '39.99', '99.00', 45, 15)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (10,'Francuska', 'Pariz', '02/05/2022 7:00 AM', 'Srbija', 'Beograd', '02/05/2022 8:30 PM', '38.99', '89.99', 40, 12)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (4,'Srbija', 'Beograd', '01/29/2022 5:00 PM', 'Italija', 'Katanija', '01/29/2022 6:45 PM', '29.99', '89.99', 23, 5)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (11,'Italija', 'Katanija', '02/09/2022 9:00 AM', 'Srbija', 'Beograd', '02/09/2022 10:20 AM', '29.99', '109.99', 25, 10)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (5,'Srbija', 'Nis', '01/21/2022 3:00 PM', 'Italija', 'Milano', '01/21/2022 4:15 PM', '39.99', '55.99', 45, 22)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (12,'Italija', 'Milano', '01/30/2022 11:00 AM', 'Srbija', 'Nis', '01/30/2022 1:00 PM', '69.99', '129.99', 44, 14)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (6,'Srbija', 'Nis', '01/22/2022 1:00 PM', 'Italija', 'Rim', '01/22/2022 2:15 PM', '39.99', '59.99', 53, 23)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (13,'Italija', 'Rim', '01/30/2022 11:00 AM', 'Srbija', 'Nis', '01/30/2022 1:00 PM', '69.99', '129.99', 60, 22)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (7,'Srbija', 'Nis', '01/29/2022 3:00 PM', 'Austrija', 'Bec', '01/29/2022 4:15 PM', '49.99', '69.99', 5, 10)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (14,'Austrija', 'Bec', '02/05/2022 4:00 PM', 'Srbija', 'Nis', '02/05/2022 5:30 PM', '49.99', '89.99', 16, 5)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (15,'Bosna i Hercegovina', 'Tuzla', '01/22/2022 6:00 AM', 'Austrija', 'Bec', '01/22/2022 7:30 PM', '49.99', '79.99', 10, 10)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (20,'Austrija', 'Bec', '01/28/2022 4:00 PM', 'Bosna i Hercegovina', 'Tuzla', '01/28/2022 5:30 PM', '49.99', '99.99', 22, 12)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (16,'Bosna i Hercegovina', 'Tuzla', '01/23/2022 3:00 AM', 'Nemacka', 'Berlin', '01/23/2022 4:30 PM', '29.99', '59.99', 23, 5)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (21,'Nemacka', 'Berlin', '02/01/2022 1:00 PM', 'Bosna i Hercegovina', 'Tuzla', '02/01/2022 2:30 PM', '39.99', '89.99', 34, 11)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (17,'Bosna i Hercegovina', 'Sarajevo', '02/04/2022 6:00 AM', 'Nemacka', 'Minhen', '02/04/2022 7:30 PM', '49.99', '79.99', 33, 5)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (22,'Nemacka', 'Minhen', '02/11/2022 4:00 AM', 'Bosna i Hercegovina', 'Sarajevo', '02/11/2022 5:30 AM', '59.99', '129.99', 21, 15)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (18,'Bosna i Hercegovina', 'Sarajevo', '01/27/2022 6:00 PM', 'Francuska', 'Pariz', '01/27/2022 7:30 PM', '59.99', '99.99', 23, 3)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (23,'Francuska', 'Pariz', '02/05/2022 4:00 PM', 'Bosna i Hercegovina', 'Sarajevo', '02/05/2022 5:30 PM', '49.99', '119.99', 59, 23)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (19,'Hrvatska', 'Zagreb', '01/22/2022 6:00 PM', 'Srbija', 'Beograd', '01/22/2022 7:00 PM', '29.99', '59.99', 4, 22)");
        MyDB.execSQL("Insert into flights(id,departureCountry,departureCity,departureDateTime,flightForCountry,flightForCity,flightForDateTime,businessTicket,firstClassTicket,businessTicketNumber,firstClassTicketNumber) VALUES (24,'Srbija', 'Beograd', '01/28/2022 4:00 PM', 'Hrvatska', 'Zagreb', '01/28/2022 5:30 PM', '49.99', '89.99', 12, 27)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertDataFlight(String departureCountry, String departureCity, String departureDateTime, String flightForCountry, String flightForCity, String flightForDateTime, Double businessTicket, Double firstClassTicket, int businessTicketNumber, int firstClassTicketNumber) {
        SQLiteDatabase MYDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("departureCountry", departureCountry);
        contentValues.put("departureCity", departureCity);
        contentValues.put("departureDateTime", departureDateTime);
        contentValues.put("flightForCountry", flightForCountry);
        contentValues.put("flightForCity", flightForCity);
        contentValues.put("flightForDateTime", flightForDateTime);
        contentValues.put("businessTicket", businessTicket);
        contentValues.put("firstClassTicket", firstClassTicket);
        contentValues.put("businessTicketNumber", businessTicketNumber);
        contentValues.put("firstClassTicketNumber", firstClassTicketNumber);
        long result = MYDB.insert("flights", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertData(String name, String surname, String username, String email, String password, String position, byte[] image, boolean is_logged, double money) {
        SQLiteDatabase MYDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("position", position);
        contentValues.put("image", image);
        contentValues.put("is_logged", is_logged);
        contentValues.put("money", money);
        long result = MYDB.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertDataTicketReservation(int id_user, int id_flight, String ticket_class, String departureFrom, String flightFor, String dateAndTimeDeparture, String check_in_user_name, String check_in_user_surname, String check_in_user_passport_number, String check_in_user_nationality, boolean checkIn, double ticketPrice) {
        SQLiteDatabase MYDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_user", id_user);
        contentValues.put("id_flight", id_flight);
        contentValues.put("ticket_class", ticket_class);
        contentValues.put("departureFrom", departureFrom);
        contentValues.put("flightFor", flightFor);
        contentValues.put("dateAndTimeDeparture", dateAndTimeDeparture);
        contentValues.put("check_in_user_name", check_in_user_name);
        contentValues.put("check_in_user_surname", check_in_user_surname);
        contentValues.put("check_in_user_passport_number", check_in_user_passport_number);
        contentValues.put("check_in_user_nationality", check_in_user_nationality);
        contentValues.put("checkIn", checkIn);
        contentValues.put("ticketPrice", ticketPrice);
        long result = MYDB.insert("ticketsReserved", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public User setLoginUserData(String username, String password){
        User user = null;
        try{
            SQLiteDatabase MyDB = this.getWritableDatabase();
            Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
            if(cursor.moveToFirst()){
                user = new User();
                user.setUserId(cursor.getInt(0));
                user.setUserName(cursor.getString(1));
                user.setUserSurname(cursor.getString(2));
                user.setUserUsername(cursor.getString(3));
                user.setUserEmail(cursor.getString(4));
                user.setUserPassword(cursor.getString(5));
                user.setUserPosition(cursor.getString(6));
                user.setIsLogged(cursor.getInt(8) > 0);
                user.setUserMoney(cursor.getDouble(9));
            }
        } catch (Exception e){
            user = null;
        }
        return user;
    }

    public Bitmap getImage(Integer id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Bitmap bitmap = null;
        Cursor cursor = MyDB.rawQuery("select * from users where id=?", new String[]{String.valueOf(id)});
        if(cursor.moveToNext()){
            byte[] image = cursor.getBlob(7);
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        return bitmap;
    }
    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUsernameAndPassword(String username, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void updateUserInfo(int id, String username, String name, String surname, String email, String password, String position, byte[] image, boolean is_logged, double money) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("position", position);
        contentValues.put("image", image);
        contentValues.put("is_logged", is_logged);
        contentValues.put("money", money);
        MyDB.update("users", contentValues, "id = ?", new String[]{String.valueOf(id)});
        MyDB.close();
    }

    public void updateUserMoney(int id_user, double money) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("money", money);
        MyDB.update("users", contentValues, "id = ?", new String[]{String.valueOf(id_user)});
        MyDB.close();
    }

    public void updateUserIsLogged(int id, boolean is_logged) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_logged", is_logged);
        MyDB.update("users", contentValues, "id = ?", new String[]{String.valueOf(id)});
        MyDB.close();
    }

    public void updateFlightInfo(int id, String departureCountry, String departureCity, String departureDateTime, String flightForCountry, String flightForCity, String flightForDateTime, Double businessTicket, Double firstClassTicket, int businessTicketNumber, int firstClassTicketNumber) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("departureCountry", departureCountry);
        contentValues.put("departureCity", departureCity);
        contentValues.put("departureDateTime", departureDateTime);
        contentValues.put("flightForCountry", flightForCountry);
        contentValues.put("flightForCity", flightForCity);
        contentValues.put("flightForDateTime", flightForDateTime);
        contentValues.put("businessTicket", businessTicket);
        contentValues.put("firstClassTicket", firstClassTicket);
        contentValues.put("businessTicketNumber", businessTicketNumber);
        contentValues.put("firstClassTicketNumber", firstClassTicketNumber);
        MyDB.update("flights", contentValues, "id = ?", new String[]{String.valueOf(id)});
        MyDB.close();
    }

    public void updateTicketReservationInfo(int id, String check_in_user_name, String check_in_user_surname, String check_in_user_passport_number, String check_in_user_nationality, boolean checkIn) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("check_in_user_name", check_in_user_name);
        contentValues.put("check_in_user_surname", check_in_user_surname);
        contentValues.put("check_in_user_passport_number", check_in_user_passport_number);
        contentValues.put("check_in_user_nationality", check_in_user_nationality);
        contentValues.put("checkIn", checkIn);
        MyDB.update("ticketsReserved", contentValues, "id = ?", new String[]{String.valueOf(id)});
        MyDB.close();
    }

    public void updateBusinessTicketNumber(int id, int numberTicket) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("businessTicketNumber", numberTicket);
        MyDB.update("flights", contentValues, "id = ?", new String[]{String.valueOf(id)});
        MyDB.close();
    }

    public void updateFirstClassTicketNumber(int id, int numberTicket) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstClassTicketNumber", numberTicket);
        MyDB.update("flights", contentValues, "id = ?", new String[]{String.valueOf(id)});
        MyDB.close();
    }

    public void activateAdmin(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("position", "admin");
        MyDB.update("users", contentValues, "username = ?", new String[]{username});
        MyDB.close();
    }

    public void deleteUser(int id){
        SQLiteDatabase MyDB = getWritableDatabase();
        String sql = "DELETE FROM users WHERE id=?";
        SQLiteStatement statement = MyDB.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);
        statement.execute();
        MyDB.close();
    }

    public void deleteFlight(int id){
        SQLiteDatabase MyDB = getWritableDatabase();
        String sql = "DELETE FROM flights WHERE id=?";
        SQLiteStatement statement = MyDB.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);
        statement.execute();
        MyDB.close();
    }

    public void deleteReservationTicket(int id){
        SQLiteDatabase MyDB = getWritableDatabase();
        String sql = "DELETE FROM ticketsReserved WHERE id=?";
        SQLiteStatement statement = MyDB.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);
        statement.execute();
        MyDB.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        return MyDB.rawQuery(sql, null);
    }

    public void queryData(String sql){
        SQLiteDatabase MyDB = getWritableDatabase();
        MyDB.execSQL(sql);
    }

    public User getUserData(int id){
        User user = new User();
        try{
            SQLiteDatabase MyDB = this.getReadableDatabase();
            Cursor cursor = MyDB.rawQuery("Select * from users where id = ?", new String[]{String.valueOf(id)});
            if(cursor.moveToFirst()){
                user = new User();
                user.setUserId(cursor.getInt(0));
                user.setUserName(cursor.getString(1));
                user.setUserSurname(cursor.getString(2));
                user.setUserUsername(cursor.getString(3));
                user.setUserEmail(cursor.getString(4));
                user.setUserPassword(cursor.getString(5));
                user.setUserPosition(cursor.getString(6));
                user.setIsLogged(cursor.getInt(8) > 0);
                user.setUserMoney(cursor.getDouble(9));
            }
        } catch (Exception e){
            user = null;
        }
        return user;
    }

    public Flights getFlightsData(int id){
        Flights flights = new Flights();
        try{
            SQLiteDatabase MyDB = this.getReadableDatabase();
            Cursor cursor = MyDB.rawQuery("Select * from flights where id = ?", new String[]{String.valueOf(id)});
            if(cursor.moveToFirst()){
                flights = new Flights();
                flights.set_id(cursor.getInt(0));
                flights.setFlightDepartureCountry(cursor.getString(1));
                flights.setFlightDepartureCity(cursor.getString(2));
                flights.setFlightDepartureDateAndTime(cursor.getString(3));
                flights.setFlightForCountry(cursor.getString(4));
                flights.setFlightForCity(cursor.getString(5));
                flights.setFlightForDateAndTime(cursor.getString(6));
                flights.setFlightBusinessTicket(cursor.getDouble(7));
                flights.setFlightFirstClassTicket(cursor.getDouble(8));
                flights.setFlightBusinessTicketNumber(cursor.getInt(9));
                flights.setFlightFirstClassTicketNumber(cursor.getInt(10));
            }
        } catch (Exception e){
            flights = null;
        }
        return flights;
    }

    public TicketReservation getTicketReservationData(int id){
        TicketReservation ticketReservation = new TicketReservation();
        try{
            SQLiteDatabase MyDB = this.getReadableDatabase();
            Cursor cursor = MyDB.rawQuery("Select * from ticketsReserved where id = ?", new String[]{String.valueOf(id)});
            if(cursor.moveToFirst()){
                ticketReservation = new TicketReservation();
                ticketReservation.setTicketReservationID(cursor.getInt(0));
                ticketReservation.setTicketReservationIDUser(cursor.getInt(1));
                ticketReservation.setTicketReservationIDFlight(cursor.getInt(2));
                ticketReservation.setTicketReservationTicketClass(cursor.getString(3));
                ticketReservation.setTicketReservationDepartureFrom(cursor.getString(4));
                ticketReservation.setTicketReservationFlightFor(cursor.getString(5));
                ticketReservation.setTicketReservationDepartureDateAndTime(cursor.getString(6));
                ticketReservation.setTicketReservationCheckInUserName(cursor.getString(7));
                ticketReservation.setTicketReservationCheckInUserSurname(cursor.getString(8));
                ticketReservation.setTicketReservationCheckInUserPassportNumber(cursor.getString(9));
                ticketReservation.setTicketReservationCheckInUserNationality(cursor.getString(10));
                ticketReservation.setTicketReservationCheckIn(cursor.getInt(11) > 0);
                ticketReservation.setTicketPrice(cursor.getDouble(12));
            }
        } catch (Exception e){
            ticketReservation = null;
        }
        return ticketReservation;
    }
}
