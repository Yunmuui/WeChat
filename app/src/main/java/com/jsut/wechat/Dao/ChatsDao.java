package com.jsut.wechat.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.Contact;
import com.jsut.wechat.fragment.ChatsFragment;

import java.util.List;

@Dao
public interface ChatsDao {
    //添加新聊天到本地聊天记录
    @Insert
    public void insert(Chat chat);

    @Delete
    public void delete(Chat chat);

    @Delete
    void deleteAll(List<Chat> entities);

//    @Query("SELECT * FROM Contacts Where user=:user and friend=:friend")
//    List<Contact> getALLDelete(String user,String friend);
//
    //查询用户本地聊天记录
    @Query("SELECT * FROM Chats Where user=:user")
    List<Chat> getChatsList(String user);

    //更新本地聊天记录

}
