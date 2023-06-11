package com.jsut.wechat.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jsut.wechat.Dao.ChatsDao;
import com.jsut.wechat.Dao.RemoteMsgDao;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;

@Database(entities = OneMsg.class,version = 1,exportSchema=false)
public abstract class RemoteMsgDatabase extends RoomDatabase {
    private static RemoteMsgDatabase databaseInstance;
    private static final String DATABASE_NAME = "RemoteMsg_db";

    //结合单例模式完成创建数据库实例
    public static synchronized RemoteMsgDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    RemoteMsgDatabase.class,DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        }
        return databaseInstance;
    }
    public abstract RemoteMsgDao getRemoteMsgDao();
}
