package com.jsut.wechat.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RemoteMsg")
public class OneMsg implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "sender")
    @NonNull
    private String sender;
    @ColumnInfo(name = "receiver")
    @NonNull
    private String receiver;
    @ColumnInfo(name = "chatType")
    @NonNull
    private String chatType;
    @ColumnInfo(name = "chatContent")
    @NonNull
    private String chatContent;
    @ColumnInfo(name = "time")
    @NonNull
    private String time;

    public OneMsg(@NonNull String sender, @NonNull String receiver, @NonNull String chatType, @NonNull String chatContent, @NonNull String time) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(sender);
        dest.writeString(receiver);
        dest.writeString(chatType);
        dest.writeString(chatContent);
        dest.writeString(time);
    }
    protected OneMsg(Parcel in) {
        sender = in.readString();
        receiver = in.readString();
        chatType = in.readString();
        chatContent = in.readString();
        time = in.readString();
    }
    public static final Parcelable.Creator<OneMsg> CREATOR = new Parcelable.Creator<OneMsg>() {
        @Override
        public OneMsg createFromParcel(Parcel in) {
            return new OneMsg(in);
        }

        @Override
        public OneMsg[] newArray(int size) {
            return new OneMsg[size];
        }
    };
}
