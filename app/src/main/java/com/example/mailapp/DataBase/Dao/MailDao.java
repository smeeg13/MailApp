package com.example.mailapp.DataBase.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mailapp.DataBase.Entities.Mail;

import java.util.List;

@Dao
public interface MailDao {
    @Query("SELECT * FROM Mail")
    List<Mail> getAll();

    @Query("SELECT * FROM Mail WHERE iD_Mail = :id")
    List<Mail> getAllById(int id);

    @Insert
    void insertAll(Mail mail);
}
