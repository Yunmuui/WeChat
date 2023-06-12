package com.jsut.wechat.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jsut.wechat.Entity.Contact;

import java.util.List;

@Dao
public interface ContactsDao {
    @Insert
    public void insert(Contact contact);

    @Delete
    public void delete(Contact Contact);

    @Delete
    void deleteAll(List<Contact> entities);
    @Query("DELETE FROM Contacts")
    void deleteAll();

    @Query("SELECT * FROM Contacts Where user=:user and friend=:friend")
    List<Contact> getALLDelete(String user,String friend);

    @Query("SELECT DISTINCT friend FROM Contacts Where user=:user")
    List<String> getFriendList(String user);
}
