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
        int Id = 0 ;// 获取聊天id
        intent.putExtra("user",username);
        intent.putExtra("chatTitle", chatTitle);
        intent.putExtra("id", Id);
        intent.setClass(FriendDetailActivity.this,ChatActivity.class);
        startActivity(intent);
    }

    public void friend_detail_back_onClick(View view) {
        finish();
    }
}