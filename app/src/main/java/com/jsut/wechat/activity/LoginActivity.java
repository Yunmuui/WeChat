package com.jsut.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jsut.wechat.Dao.UserDao;
import com.jsut.wechat.DataBase.UserDatabase;
import com.jsut.wechat.Entity.User;
import com.jsut.wechat.R;

import java.util.List;

//登陆界面
public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText logphone,logpassword;
    public Button login;
    private User view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar=findViewById(R.id.back_homepage);
        logphone=findViewById(R.id.et_phone);
        logpassword=findViewById(R.id.et_password);
        login=findViewById(R.id.login_into);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,HomepageActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDao userDao = UserDatabase.getDatabaseInstance(LoginActivity.this).getUserDao();
                User user=new User();
                user.phone=logphone.getText().toString().trim();
                user.password=logpassword.getText().toString().trim();
                List<User> user1List = userDao.getallUserByPhone(user.phone);
                for (User user1 : user1List){
                    if(user.password.equals(user1.password)){
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", user.phone);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return;
                    }
                }
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();

            }
        });
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            ((Window) window).addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));

        }
        //设置状态栏图标颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    
}