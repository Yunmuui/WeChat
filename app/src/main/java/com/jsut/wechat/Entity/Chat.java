package com.jsut.wechat.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.jsut.wechat.Converter.ChatTypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Chats")
@TypeConverters({ChatTypeConverters.class})
public class Chat {
    //id
    @PrimaryKey(autoGenerate = true)
    public int id;

    //所属用户
    @ColumnInfo(name = "user")
    @NonNull
    public String user;

    //聊天标题
    @ColumnInfo(name = "chatTitle")
    @NonNull
    public String chatTitle;

    //聊天缩略
    @ColumnInfo(name = "chatAbbreviation")
    @NonNull
    public String chatAbbreviation;

    //最后聊天时间
    @ColumnInfo(name = "chatTime")
    @NonNull
    public String chatTime;

    //聊天内容
    @ColumnInfo(name = "chatContent")
    @NonNull
    @TypeConverters(ChatTypeConverters.class)
    public List<OneMsg> chatContent;

    public Chat(@NonNull String user, @NonNull String chatTitle, @NonNull String chatAbbreviation, @NonNull String chatTime, @NonNull List<OneMsg> chatContent) {
        this.user = user;
        this.chatTitle = chatTitle;
        this.chatAbbreviation = chatAbbreviation;
        this.chatTime = chatTime;
        this.chatContent = chatContent;
    }
    public void addOneMsg(OneMsg e){
        chatContent.add(e);
    }
    @Ignore
    public Chat() {

    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    @NonNull
    public String getChatTitle() {
        return chatTitle;
    }

    @NonNull
    public String getChatAbbreviation() {
        return chatAbbreviation;
    }

    @NonNull
    public String getChatTime() {
        return chatTime;
    }

    @NonNull
    public List<OneMsg> getChatContent() {
        return chatContent;
    }
}
