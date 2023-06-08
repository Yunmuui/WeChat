package com.jsut.wechat.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jsut.wechat.Dao.ContactsDao;
import com.jsut.wechat.Entity.Contact;

@Database(entities = Contact.class,version = 2,exportSchema=false)
public abstract class ContactsDataBase extends RoomDatabase {
    private static ContactsDataBase databaseInstance;
    private static final String DATABASE_NAME = "Contacts_db";

    //结合单例模式完成创建数据库实例
    public static synchronized ContactsDataBase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ContactsDataBase.class,DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        }
        return databaseInstance;
    }
    public abstract ContactsDao getContactsDao();
}
