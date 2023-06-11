package com.jsut.wechat.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;

import java.util.List;

@Dao
public interface RemoteMsgDao {
    //将新聊天送到远程服务器，待接受
    @Insert
    public void insert(OneMsg oneMsg);

    @Delete
    public void delete(OneMsg oneMsg);

    @Delete
    void deleteAll(List<OneMsg> entities);

    //查询当前用户的新消息
    @Query("SELECT * FROM RemoteMsg Where receiver=:user")
    List<OneMsg> getMsgList(String user);
}
