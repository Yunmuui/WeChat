package com.jsut.wechat.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.Contact;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.Entity.User;
import com.jsut.wechat.fragment.ChatsFragment;

import java.util.List;

@Dao
public interface ChatsDao {
    //添加新聊天到本地聊天记录
    @Insert
    public void insert(Chat chat);

    @Insert
    public long insertAndReturnId(Chat chat);

    @Delete
    public void delete(Chat chat);

    @Delete
    void deleteAll(List<Chat> entities);
    @Query("DELETE FROM Chats")
    void deleteAll();
//    @Query("SELECT * FROM Contacts Where user=:user and friend=:friend")
//    List<Contact> getALLDelete(String user,String friend);
//
    //查询用户本地聊天记录
    @Query("SELECT * FROM Chats Where user=:user")
    List<Chat> getChatsListByUser(String user);

    //查询用户本地聊天记录
    @Query("SELECT * FROM Chats Where id=:id")
    Chat getChatById(int id);

    //更新本地聊天记录
    @Update
    void updateContent(Chat newChat);
}
