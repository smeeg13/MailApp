package com.example.mailapp.DataBase.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PostWorker")
public class PostWorker {

    public PostWorker(String firstname, String lastName, String phone, String login, String password, String address, String zip, String region) {
        this.firstname = firstname;
        this.lastName = lastName;
        this.phone = phone;
        this.login = login;
        this.password = password;
        this.address = address;
        this.zip = zip;
        this.region = region;
    }

    @PrimaryKey(autoGenerate = true)
    public int iD_PostWorker;

    @ColumnInfo(name = "firstname")
    public String firstname;
    @ColumnInfo(name = "lastName")
    public String lastName;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "login")
    public String login;
    @ColumnInfo(name = "password")
    public String password;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "zip")
    public String zip;
    @ColumnInfo(name = "region")
    public String region;

}
