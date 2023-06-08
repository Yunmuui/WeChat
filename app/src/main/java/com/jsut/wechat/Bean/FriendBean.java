package com.jsut.wechat.Bean;

public class FriendBean {
    private String friend_profile_picture;
    private String friend_name;
    private String friend_text;
    private String friend_publish_time;

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_text() {
        return friend_text;
    }

    public void setFriend_text(String friend_text) {
        this.friend_text = friend_text;
    }

    public String getFriend_publish_time() {
        return friend_publish_time;
    }

    public void setFriend_publish_time(String friend_publish_time) {
        this.friend_publish_time = friend_publish_time;
    }

    public String getFriend_profile_picture() {
        return friend_profile_picture;
    }

    public void setFriend_profile_picture(String friend_profile_picture) {
        this.friend_profile_picture = friend_profile_picture;
    }
}
