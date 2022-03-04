package com.example.mailapp.DataBase.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mailapp.DataBase.Tables.PostWorker;

import java.util.List;

@Dao
public interface PostWorkerDao {

    //Its like the DAL layer

    @Query("SELECT * FROM PostWorker")
    List<PostWorker> getAll();
    // the :userIds is the input that we give in the method that we call
    @Query("SELECT * FROM PostWorker WHERE iD_PostWorker IN (:userIds)")
    List<PostWorker> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM PostWorker WHERE iD_PostWorker = :id")
    PostWorker getPostWorkerByid(int id);

    @Query("SELECT * FROM PostWorker WHERE firstname LIKE :first AND " +
            "lastname LIKE :last LIMIT 1")
    PostWorker findByName(String first, String last);

    @Query("DELETE FROM PostWorker")
    void deleteAll();

    @Query("UPDATE PostWorker SET region = :region, zip = :zip, address = :address, phone = :phone WHERE iD_PostWorker = :idPostWorker")
    void updatePostWorker(int idPostWorker,String region,String zip, String address, String phone);

    @Insert
    void insertAll(PostWorker... postWorkers);

    @Insert
    void addPostWorker(PostWorker postWorker);

    @Delete
    void deletePostWorker(PostWorker postWorker);

    @Update
    void updatePostWorker(PostWorker postWorker);


}