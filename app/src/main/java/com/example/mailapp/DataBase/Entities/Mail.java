package com.example.mailapp.DataBase.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity (tableName = "Mail")
public class Mail {
//
    public Mail() {
    }

    @PrimaryKey(autoGenerate = true)
    public int iD_Mail;

    //TODO add the link with postworker

    @ColumnInfo(name = "mailFrom")
    public String mailFrom;
    @ColumnInfo(name = "mailTo")
    public String mailTo;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "zip")
    public String zip;
    @ColumnInfo(name = "locationName")
    public String locationName;
    @ColumnInfo(name = "shippingType")
    public String shippingType;
    @ColumnInfo(name = "mailType")
    public String mailType;
    @ColumnInfo(name = "status")
    public String status;
    @ColumnInfo(name = "receiveDate")
    public String receiveDate;
    @ColumnInfo(name = "shippedDate")
    public String shippedDate;


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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public int getiD_Mail() {
        return iD_Mail;
    }

    public  void MailToString(Mail mail) {

        System.out.println("/////////////////");
        System.out.println("ID of mail : " + mail.getiD_Mail());
        System.out.println("Mail to :" + mail.getMailTo());
        System.out.println("Mail from :" + mail.getMailFrom());
        System.out.println("Address :" + mail.getAddress());
        System.out.println("Location :" + mail.getLocationName());
        System.out.println("Zip :" + mail.getZip());
        System.out.println("Shipped date :" + mail.getShippedDate());
        System.out.println("Shipping type :" + mail.getShippingType());
        System.out.println("Receive date :" + mail.getReceiveDate());
        System.out.println("Status :" + mail.getStatus());


    }

    public void mailsToString(List<Mail> mails) {

        for (Mail mail : mails) {

            System.out.println("/////////////////");
            System.out.println("ID of mail : " + mail.getiD_Mail());
            System.out.println("Mail to :" + mail.getMailTo());
            System.out.println("Mail from :" + mail.getMailFrom());
            System.out.println("Address :" + mail.getAddress());
            System.out.println("Location :" + mail.getLocationName());
            System.out.println("Zip :" + mail.getZip());
            System.out.println("Shipped date :" + mail.getShippedDate());
            System.out.println("Shipping type :" + mail.getShippingType());
            System.out.println("Receive date :" + mail.getReceiveDate());
            System.out.println("Status :" + mail.getStatus());
        }


    }

    public void setCity(String toString) {
    }
}
