package com.jsut.wechat.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jsut.wechat.Entity.User;

import java.util.List;
@Dao
public interface UserDao {
    @Insert
//增
    void inserUser(User user);
    @Delete
//删
    void  deleteUser(User user);
    @Update
//改
    void  update(User user);

    @Query("SELECT * FROM User")
    List<User> getallUser() ;
    //查
    @Query("SELECT * FROM User where id= :id")
    List<User> getallUserById(int id);

    @Query("SELECT * FROM User where phone like  '%'||:phone||'%'")
    List<User> getallUserByPhone(String phone) ;

}
