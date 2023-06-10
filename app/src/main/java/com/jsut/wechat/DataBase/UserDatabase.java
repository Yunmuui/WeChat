package com.jsut.wechat.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jsut.wechat.Dao.UserDao;
import com.jsut.wechat.Entity.User;

import java.security.AccessControlContext;

@Database(entities = {User.class}, version = 2, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase databaseInstance;
    private static final String DATABASE_NAME = "User_db";

    //结合单例模式完成创建数据库实例
    public static synchronized UserDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                   UserDatabase.class,DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        }
        return databaseInstance;
    }

    //结合单例模式完成创建数据库实例

    public abstract UserDao getUserDao();

}

