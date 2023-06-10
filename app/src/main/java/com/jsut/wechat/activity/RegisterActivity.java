package com.jsut.wechat.activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    }


}