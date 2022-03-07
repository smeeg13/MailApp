package com.example.mailapp.DataBase.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

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
    @ColumnInfo(name = "background")
    public String background;

    public PostWorker() {

    }

    public int getiD_PostWorker() {
        return iD_PostWorker;
    }

    public void setiD_PostWorker(int iD_PostWorker) {
        this.iD_PostWorker = iD_PostWorker;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void postWorkerToString(PostWorker postWorker) {

        System.out.println("/////////////////");
        System.out.println("ID of post worker : " + postWorker.getiD_PostWorker());
        System.out.println("Login :" + postWorker.getLogin());
        System.out.println("Firstname :" + postWorker.getFirstname());
        System.out.println("Lastname :" + postWorker.getLastName());
        System.out.println("Phone :" + postWorker.getPhone());
        System.out.println("Address :" + postWorker.getAddress());
        System.out.println("Region :" + postWorker.getRegion());
        System.out.println("Zip :" + postWorker.getZip());
        System.out.println("Background :" + postWorker.getBackground());

    }

    public  void postWorkersListToString(List<PostWorker> postWorkers) {
        for (PostWorker postWorker : postWorkers) {
            System.out.println("/////////////////");
            System.out.println("ID of post worker : " + postWorker.getiD_PostWorker());
            System.out.println("Login :" + postWorker.getLogin());
            System.out.println("Firstname :" + postWorker.getFirstname());
            System.out.println("Lastname :" + postWorker.getLastName());
            System.out.println("Phone :" + postWorker.getPhone());
            System.out.println("Address :" + postWorker.getAddress());
            System.out.println("Region :" + postWorker.getRegion());
            System.out.println("Zip :" + postWorker.getZip());
            System.out.println("Background :" + postWorker.getBackground());

        }
    }
}
