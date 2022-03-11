package com.example.mailapp.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;

import java.util.List;

@Dao
public interface MailDao {
    @Query("SELECT * FROM Mail")
    LiveData<List<MailEntity>> getAll();

    @Query("SELECT * FROM Mail WHERE idMail = :id")
    LiveData<MailEntity> getById(int id);

    @Query("SELECT * FROM Mail WHERE postWorker = :postworker")
    LiveData<List<MailEntity>> getAllByPostworker(int postworker);


    @Query("SELECT * FROM Mail WHERE status = :status")
    LiveData<List<MailEntity>> getAllByStatus(String status);

    @Query("SELECT * FROM Mail WHERE mailType = :mailtype")
    LiveData<List<MailEntity>> getAllByMailType(String mailtype);

    @Query("SELECT * FROM Mail WHERE city = :city")
    LiveData<List<MailEntity>> getAllByCity(String city);

    @Query("SELECT * FROM Mail WHERE postWorker = :postworker")
    LiveData<MailEntity> getByPostworker(int postworker);

    @Insert
    long insert(MailEntity mailEntity)throws SQLiteConstraintException;

    @Update
    void update(MailEntity mailEntity);

    @Delete
    void delete(MailEntity mailEntity);

    @Query("DELETE FROM Mail" )
    void deleteAll();

}
