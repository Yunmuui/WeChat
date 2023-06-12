package com.jsut.wechat.activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.annotation.SuppressLint;
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
import com.jsut.wechat.DataBase.ContactsDataBase;
import com.jsut.wechat.DataBase.UserDatabase;
import com.jsut.wechat.Entity.User;
import com.jsut.wechat.R;
//注册界面
public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText rename,rephone,repassword;
    private Button register;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar=findViewById(R.id.to_homepage);
        rename=findViewById(R.id.reg_name);
        rephone=findViewById(R.id.reg_phone);
        repassword=findViewById(R.id.reg_password);
        register=findViewById(R.id.register_into);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    UserDao userDao = UserDatabase.getDatabaseInstance(RegisterActivity.this).getUserDao();
                    User user = new User();
                    user.name = rename.getText().toString().trim();
                    user.phone = rephone.getText().toString().trim();
                    user.password = repassword.getText().toString().trim();

                    userDao.inserUser(user);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
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