package com.example.mailapp.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mailapp.database.entities.PostWorkerEntity;

import java.util.List;

@Dao
public interface PostWorkerDao {

    //Its like the DAL layer

    @Query("SELECT * FROM PostWorker")
    LiveData<List<PostWorkerEntity>> getAll();

    @Query("SELECT * FROM PostWorker WHERE idPostWorker = :id")
    LiveData<PostWorkerEntity> getById(int id);

    @Query("SELECT * FROM PostWorker WHERE email = :email")
    LiveData<PostWorkerEntity> getByEmail(String email);

    @Query("SELECT * FROM PostWorker WHERE firstname LIKE :first AND " +
            "lastname LIKE :last LIMIT 1")
    LiveData<PostWorkerEntity> getByName(String first, String last);

    @Insert
    long insert(PostWorkerEntity postWorkerEntity)throws SQLiteConstraintException;

    @Update
    void update(PostWorkerEntity postWorker);

    @Delete
    void delete(PostWorkerEntity postWorker);



    @Query("DELETE FROM PostWorker" )
    void deleteAll();

    //TODO what for ?
//    // the :userIds is the input that we give in the method that we call
//    @Query("SELECT * FROM PostWorker WHERE idPostWorker IN (:userIds)")
//    List<PostWorkerEntity> loadAllByIds(int[] userIds);

    @Query("UPDATE PostWorker SET background = :background WHERE idPostWorker = :idPostWorker")
    void updatePostWorkerBackGround(int idPostWorker,String background);
}