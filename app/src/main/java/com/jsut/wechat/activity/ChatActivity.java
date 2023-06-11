package com.jsut.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.R;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    TextView chat_name;
    String user;
    RecyclerView msg_recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取传递的参数
        user = getIntent().getStringExtra("user");
        String chatTitle = getIntent().getStringExtra("chatTitle");
        List<OneMsg> chatContent = getIntent().getParcelableArrayListExtra("chatContent");
        int id = getIntent().getIntExtra("id", 0);

        //获取控件
        chat_name=findViewById(R.id.chat_name);
        msg_recycle=findViewById(R.id.msg_recycle);

        //设置控件
        chat_name.setText(chatTitle);

    }
}