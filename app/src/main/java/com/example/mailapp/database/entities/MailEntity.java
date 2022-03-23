package com.example.mailapp.database.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Entity (tableName = "Mail",
            foreignKeys = @ForeignKey(entity = PostWorkerEntity.class,
                                       parentColumns = "idPostWorker",
                                            childColumns = "idPostWorker",
                                        onDelete = ForeignKey.CASCADE
           ),
            indices = {
               @Index(
                        value = {"idPostWorker"}
                )}
)



public class MailEntity {

    private static final DateFormat df2 = new SimpleDateFormat("dd MMMM yyyy");
    private static final String todayDate = df2.format(Calendar.getInstance().getTime());

    @Ignore
    public MailEntity() {
    }

    public MailEntity(int idPostWorker,String mailFrom, String mailTo,String mailType, String shippingType, String address, String zip, String city) {
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

    @PrimaryKey(autoGenerate = true)
    public int idMail;

    //TODO add the link with postworker
    @ColumnInfo(name = "idPostWorker")
    public int idPostWorker;

    @ColumnInfo(name = "mailFrom")
    public String mailFrom;
    @ColumnInfo(name = "mailTo")
    public String mailTo;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "zip")
    public String zip;
    @ColumnInfo(name = "city")
    public String city;
    @ColumnInfo(name = "shippingType")
    public String shippingType;
    @ColumnInfo(name = "mailType")
    public String mailType;
    @ColumnInfo(name = "weight")
    public int weight;
    @ColumnInfo(name = "status")
    public String status;
    @ColumnInfo(name = "receiveDate")
    public String receiveDate;
    @ColumnInfo(name = "shippedDate")
    public String shippedDate;

    public int getIdPostWorker() {
        return idPostWorker;
    }

    public void setIdPostWorker(int idPostWorker) {
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

    public int getIdMail() {
        return idMail;
    }


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
                "Location : " + city+
                "Zip : " + zip+
                "Mail type : " + mailType+
                "weight : " + weight+
                "Shipping type : " + shippingType+
                "Shipped date : " + shippedDate+
                "Receive date in central : " + receiveDate+
                "Status : " + status+
                "/////////////////";
    }

    public void mailsToString(List<MailEntity> mailEntities) {

        for (MailEntity mailEntity : mailEntities) {

            System.out.println("/////////////////");
            System.out.println("ID of mail : " + mailEntity.getIdMail());
            System.out.println("Mail to :" + mailEntity.getMailTo());
            System.out.println("Mail from :" + mailEntity.getMailFrom());
            System.out.println("Address :" + mailEntity.getAddress());
            System.out.println("Location :" + mailEntity.getCity());
            System.out.println("Zip :" + mailEntity.getZip());
            System.out.println("Mail type :" + mailEntity.getMailType());
            System.out.println("Weight :" + mailEntity.getWeight());
            System.out.println("Shipping type :" + mailEntity.getShippingType());
            System.out.println("Shipped date :" + mailEntity.getShippedDate());
            System.out.println("Receive date :" + mailEntity.getReceiveDate());
            System.out.println("Status :" + mailEntity.getStatus());
        }
    }


}
