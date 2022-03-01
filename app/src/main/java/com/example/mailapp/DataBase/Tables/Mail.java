package com.example.mailapp.DataBase.Tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Mail {
//
    public Mail() {
    }

    @PrimaryKey(autoGenerate = true)
    public int iD_Mail;

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

}
