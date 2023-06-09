package com.jsut.wechat.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public  String phone;
    public String name;
    public  String password;

}