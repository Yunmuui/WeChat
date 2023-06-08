package com.jsut.wechat.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.jsut.wechat.Bean.FriendBean;
import com.jsut.wechat.R;
import com.jsut.wechat.adapter.WeChatAdapter;
import com.thoughtworks.xstream.XStream;

import java.io.InputStream;
import java.util.ArrayList;

public class WeChatMomentsActivity extends AppCompatActivity {
    private String loginStatus;
    RecyclerView recyclerView;
    WeChatAdapter weChatAdapter;
    TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_moments);

        loginStatus = getIntent().getStringExtra("loginStatus");

        XStream xStream = new XStream();
        xStream.alias("FriendBean", FriendBean.class);  // 指定别名
        InputStream inputStream = getResources().openRawResource(R.raw.moments);
        ArrayList<FriendBean> friendList = (ArrayList<FriendBean>) xStream.fromXML(inputStream);
        weChatAdapter = new WeChatAdapter(friendList, R.drawable.cover_image,loginStatus);

        //获得recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(weChatAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        //设置状态栏颜色
        initStatusBar();
    }



    protected void initStatusBar() {
        //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
        boolean useThemestatusBarColor = false;
        //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
        boolean useStatusBarColor = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemestatusBarColor) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏背景色
            } else {
                getWindow().setStatusBarColor(Color.TRANSPARENT);//透明
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        } else {
            Toast.makeText(this, "低于4.4的android系统版本不存在沉浸式状态栏", Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && useStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}