package com.example.mailapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "PostWorker")
public class PostWorkerEntity {

    public PostWorkerEntity(String firstname, String lastname, String phone, String email, String password, String address, String zip, String city) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.zip = zip;
        this.city = city;
    }

    @PrimaryKey(autoGenerate = true)
    public int idPostWorker;

    @ColumnInfo(name = "firstname")
    public String firstname;
    @ColumnInfo(name = "lastname")
    public String lastname;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "password")
    public String password;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "zip")
    public String zip;
    @ColumnInfo(name = "city")
    public String city;
    @ColumnInfo(name = "background")
    public String background;

    @Ignore
    public PostWorkerEntity() {

    }


    public int getIdPostWorker() {
        return idPostWorker;
    }

    public void setIdPostWorker(int idPostWorker) {
        this.idPostWorker = idPostWorker;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof PostWorkerEntity)) return false;
        PostWorkerEntity o = (PostWorkerEntity) obj;
        return o.getIdPostWorker() == (this.getIdPostWorker());
    }

    @Override
    public String toString() {
        String sep = System.getProperty("line.separator");
        return "/////////////////"+sep+
                "ID : " + idPostWorker+sep+
                " Email : " + email+sep+
                "Firstame : " + firstname+sep+
                "Lastname : " + lastname+sep+
                "Phone number : " + phone+sep+
                "Address : "+ address+sep+
                "City : " + city+sep+
                "Zip : " + zip+sep+
                "Backbroung settings : "+ background+sep+
                "/////////////////";
    }


    public  void postWorkersToString(List<PostWorkerEntity> postWorkerEntities) {

        for (PostWorkerEntity postWorkerEntity : postWorkerEntities) {
            System.out.println("///////////////// ");
            System.out.println(" ID of post worker : " + postWorkerEntity.getIdPostWorker());
            System.out.println(" Email :" + postWorkerEntity.getEmail());
            System.out.println(" Firstname :" + postWorkerEntity.getFirstname());
            System.out.println(" Lastname :" + postWorkerEntity.getLastname());
            System.out.println(" Phone :" + postWorkerEntity.getPhone());
            System.out.println(" Address :" + postWorkerEntity.getAddress());
            System.out.println(" City :" + postWorkerEntity.getCity());
            System.out.println(" Zip :" + postWorkerEntity.getZip());
            System.out.println(" Background :" + postWorkerEntity.getBackground());

        }
    }
}
