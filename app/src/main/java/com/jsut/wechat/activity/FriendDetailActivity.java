package com.jsut.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsSpinner;
import android.widget.TextView;

import com.jsut.wechat.Dao.ChatsDao;
import com.jsut.wechat.Dao.UserDao;
import com.jsut.wechat.DataBase.ChatsDatabase;
import com.jsut.wechat.DataBase.UserDatabase;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.Entity.User;
import com.jsut.wechat.R;
import com.jsut.wechat.adapter.ChatsAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendDetailActivity extends AppCompatActivity {
    TextView tv_friendName=null;
    String username = null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        String friendName = getIntent().getStringExtra("friendName");
        tv_friendName=(TextView)findViewById(R.id.friend_name);
        tv_friendName.setText(friendName);
        username = getIntent().getStringExtra("username");
    }

    public void sendMessageOnClick(View view) {
        Intent intent = new Intent();
        String chatTitle = tv_friendName.getText().toString();
        List<OneMsg> chatContent = new ArrayList<>(); // 获取聊天内容
        int Id = 0 ;// 获取聊天id
        if (chatContent.isEmpty()) {
            ChatsDao dao = ChatsDatabase.getDatabaseInstance(getBaseContext()).getChatsDao();
            dao.deleteAll();
            OneMsg oneChat1 = new OneMsg("用户A", "用户B", "TEXT", "你好", "2小时前");
            OneMsg oneChat2 = new OneMsg("用户B", "用户A", "TEXT", "你在哪", "1小时前");
            List<OneMsg> chatContext = new ArrayList<>();
            chatContext.add(oneChat1);
            chatContext.add(oneChat2);
            dao.insert(new Chat("用户A", "用户B", "你在哪", "1小时前",chatContent));
        }
        intent.putExtra("user",username);
        intent.putExtra("chatTitle", chatTitle);
        intent.putExtra("id", Id);
        intent.setClass(FriendDetailActivity.this,ChatActivity.class);
        startActivity(intent);
    }
}