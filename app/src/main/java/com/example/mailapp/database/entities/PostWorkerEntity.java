package com.example.mailapp.database.entities;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class PostWorkerEntity implements Comparable{
    private String idPostWorker;

    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private String address;
    private String zip;
    private String city;
    private String background;

    public PostWorkerEntity(){

    }

    public PostWorkerEntity(String firstname, String lastname, String phone, @NonNull String email, String password, String address, String zip, String city) {
        setFirstname(firstname);
        setLastname(lastname);
        setPhone(phone);
        setEmail(email);
       setPassword(password);
       setAddress(address);
       setZip(zip);
       setCity(city);
    }

    @Exclude
    public String getIdPostWorker() {
        return idPostWorker;
    }

    public void setIdPostWorker(String idPostWorker) {
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

    public void setEmail(@NonNull String email) {
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
        return "/////////////////" + sep +
                "ID : " + idPostWorker + sep +
                " Email : " + email + sep +
                "Firstame : " + firstname + sep +
                "Lastname : " + lastname + sep +
                "Phone number : " + phone + sep +
                "Address : " + address + sep +
                "City : " + city + sep +
                "Zip : " + zip + sep +
                "Backbroung settings : " + background + sep +
                "/////////////////";
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return toString().compareTo(o.toString());
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("firstname", firstname);
        result.put("lastname", lastname);
        result.put("email", email);
        result.put("phone", phone);
        result.put("address", address);
        result.put("zip", zip);
        result.put("city", city);
        result.put("background",background);
        return result;
    }
}
