package com.jsut.wechat.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
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
    public List<OneChat> chatContent;

    public Chat(@NonNull String user, @NonNull String chatTitle, @NonNull String chatAbbreviation, @NonNull String chatTime, @NonNull List<OneChat> chatContent) {
        this.user = user;
        this.chatTitle = chatTitle;
        this.chatAbbreviation = chatAbbreviation;
        this.chatTime = chatTime;
        this.chatContent = chatContent;
    }

    public static class OneChat{
        private String sender;
        private String receiver;
        private String chatType;
        private String chatContent;
        private String time;

        public OneChat(String sender, String receiver, String chatType, String chatContent, String time) {
            this.sender = sender;
            this.receiver = receiver;
            this.chatType = chatType;
            this.chatContent = chatContent;
            this.time = time;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getChatType() {
            return chatType;
        }

        public void setChatType(String chatType) {
            this.chatType = chatType;
        }

        public String getChatContent() {
            return chatContent;
        }

        public void setChatContent(String chatContent) {
            this.chatContent = chatContent;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
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
    public List<OneChat> getChatContent() {
        return chatContent;
    }
}
