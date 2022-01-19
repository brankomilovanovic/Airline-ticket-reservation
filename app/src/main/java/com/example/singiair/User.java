package com.example.singiair;

import java.io.Serializable;

public class User implements Serializable {

    private String UserName, UserSurname, UserUsername, UserEmail, UserPassword, UserPosition;
    private double UserMoney;
    private int _id;
    private byte[] Image;
    private boolean Is_logged;

    public User() {
    }

    public User(int id, String userName, String userSurname, String userEmail, String userPassword, String userUsername, String userPosition, byte[] image, boolean is_logged, double money) {
        _id = id;
        UserName = userName;
        UserSurname = userSurname;
        UserEmail = userEmail;
        UserPassword = userPassword;
        UserUsername = userUsername;
        UserPosition = userPosition;
        Image = image;
        Is_logged = is_logged;
        UserMoney = money;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getUserSurname() {
        return UserSurname;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        this.UserPassword = userPassword;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        this.UserEmail = userEmail;
    }

    public void setUserSurname(String userSurname) {
        this.UserSurname = userSurname;
    }

    public String getUserUsername() {
        return UserUsername;
    }

    public void setUserUsername(String userUsername) {
        this.UserUsername = userUsername;
    }

    public String getUserPosition() {
        return UserPosition;
    }

    public void setUserPosition(String userPosition) {
        this.UserPosition = userPosition;
    }

    public int getUserId() {
        return _id;
    }

    public void setUserId(int id) {
        this._id = id;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        this.Image = image;
    }

    public boolean getIsLogged() {
        return Is_logged;
    }

    public void setIsLogged(boolean is_logged) {
        this.Is_logged = is_logged;
    }

    public double getUserMoney() { return UserMoney; }

    public void setUserMoney(double money) { this.UserMoney = money; }
}
