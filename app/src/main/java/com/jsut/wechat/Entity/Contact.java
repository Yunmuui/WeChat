package com.jsut.wechat.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Contacts")
public class Contact {
    //用户
    @PrimaryKey(autoGenerate = true)
    public int id;

    //用户
    @ColumnInfo(name = "user")
    @NonNull
    public String user;

    //一个联系人
    @ColumnInfo(name = "friend")
    @NonNull
    public String friend;

    public Contact(@NonNull String user,@NonNull String friend) {
        this.user = user;
        this.friend = friend;
    }
}
