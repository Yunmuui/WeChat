package com.jsut.wechat.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OneMsg implements Parcelable{
    private String sender;
    private String receiver;
    private String chatType;
    private String chatContent;
    private String time;

    public OneMsg(String sender, String receiver, String chatType, String chatContent, String time) {
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
