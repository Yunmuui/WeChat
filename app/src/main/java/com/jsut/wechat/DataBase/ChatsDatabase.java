package com.jsut.wechat.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jsut.wechat.Dao.ChatsDao;
import com.jsut.wechat.Dao.ContactsDao;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.Contact;

@Database(entities = Chat.class,version = 1,exportSchema=false)
public abstract class ChatsDatabase extends RoomDatabase {
    private static ChatsDatabase databaseInstance;
    private static final String DATABASE_NAME = "Chats_db";

    //结合单例模式完成创建数据库实例
    public static synchronized ChatsDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ChatsDatabase.class,DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        }
        return databaseInstance;
    }
    public abstract ChatsDao getChatsDao();
}
