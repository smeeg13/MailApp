package com.example.mailapp.database.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;

import java.util.List;

public class PostworkerWithMails {
    @Embedded
    public PostWorkerEntity postWorker;

    @Relation(parentColumn = "idPostWorker", entityColumn = "postWorker", entity = MailEntity.class)
    public List<MailEntity> mails;
}
