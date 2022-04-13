package com.example.mailapp.database.entities;

import com.google.firebase.database.Exclude;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MailEntity {

    private static final DateFormat df2 = new SimpleDateFormat("dd MMMM yyyy");
    private static final String todayDate = df2.format(Calendar.getInstance().getTime());


    public MailEntity() {
    }

    public MailEntity(String  idPostWorker,String mailFrom, String mailTo,String mailType, String shippingType, String address, String zip, String city) {
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;
        this.address = address;
        this.zip = zip;
        this.city = city;
        this.shippingType = shippingType;
        this.receiveDate = todayDate;
        this.mailType = mailType;
        this.idPostWorker = idPostWorker;
        this.status = "In Progress";
    }


    private String idMail;

    private String idPostWorker;

    private String mailFrom;
    private String mailTo;
    private String address;
    private String zip;
    private String city;
    private String shippingType;
    private String mailType;
    private int weight;
    private String status;
    private String receiveDate;
    private String shippedDate;

    @Exclude
    public String getIdMail() {
        return idMail;
    }

    public void setIdMail(String idMail) {
        this.idMail = idMail;
    }

    @Exclude
    public String getIdPostWorker() {
        return idPostWorker;
    }

    public void setIdPostWorker(String idPostWorker) {
        this.idPostWorker = idPostWorker;
    }



    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
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

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof MailEntity)) return false;
        MailEntity o = (MailEntity) obj;
        return o.getIdMail() == (this.getIdMail());
    }

    @Override
    public String toString() {
        return "/////////////////"+
                "ID : " + idMail+
                "Mail from : " + mailFrom+
                "Mail to : " + mailTo+
                "Address : " + address+
                "City : " + city+
                "Zip : " + zip+
                "Mail type : " + mailType+
                "weight : " + weight+
                "Shipping type : " + shippingType+
                "Shipped date : " + shippedDate+
                "Receive date in central : " + receiveDate+
                "Status : " + status+
                "/////////////////";
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("mailFrom", mailFrom);
        result.put("mailTo", mailTo);
        result.put("address", address);
        result.put("city", city);
        result.put("zip", zip);
        result.put("mailType", mailType);
        result.put("weight", weight);
        result.put("shippingType", shippingType);
        result.put("shippingDate", shippedDate);
        result.put("receiveDate", receiveDate);
        result.put("status", status);

        return result;
    }
}
