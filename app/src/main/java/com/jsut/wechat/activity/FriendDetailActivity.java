package com.jsut.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jsut.wechat.R;

public class FriendDetailActivity extends AppCompatActivity {
    TextView tv_friendName=null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        String friendName = getIntent().getStringExtra("friendName");
        tv_friendName=(TextView)findViewById(R.id.friend_name);
        tv_friendName.setText(friendName);
    }

    public void sendMessageOnClick(View view) {
/*        Intent intent = new Intent();
        intent.putExtra("user",tv_friendName.getText().toString());
        intent.setClass(FriendDetailActivity.this,ChatActivity.class);
        startActivity(intent);*/
    }
}