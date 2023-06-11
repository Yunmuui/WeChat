package com.jsut.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jsut.wechat.Dao.ChatsDao;
import com.jsut.wechat.DataBase.ChatsDatabase;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.R;
import com.jsut.wechat.adapter.MsgAdapter;
import com.jsut.wechat.fragment.ChatsFragment;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    TextView chat_name;
    String user;
    RecyclerView msg_recycle;

    MsgAdapter msgAdapter;
    EditText input;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取传递的参数
        user = getIntent().getStringExtra("user");
        String chatTitle = getIntent().getStringExtra("chatTitle");
        List<OneMsg> chatContent = getIntent().getParcelableArrayListExtra("chatContent");
        //System.out.println(chatContent.get(0).getChatContent());
        int id = getIntent().getIntExtra("id", 0);

        //获取控件
        chat_name=findViewById(R.id.chat_name);
        msg_recycle=findViewById(R.id.msg_recycle);
        back=findViewById(R.id.chat_back);

        //设置控件
        chat_name.setText(chatTitle);


        //找到输入文本和发送按钮控件
        input = (EditText)findViewById(R.id.input_text);
        Button send = (Button) findViewById(R.id.send);
        //信息显示
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msg_recycle.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(chatContent,user);
        msg_recycle.setAdapter(msgAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ChatActivity.this, ChatsFragment.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(v -> {
            String content = input.getText().toString();

            if(!"".equals(content)){
                OneMsg msg =new OneMsg(user,chatTitle,"text",content,"0");
                
                chatContent.add(msg);
                //当有新消息，刷新RecyclerView的显示
                msgAdapter.notifyItemInserted(chatContent.size());
                //将RecyclerView定位到最后一行
                msg_recycle.scrollToPosition(chatContent.size());
                //清空输入框内容
                input.setText("");
            }
        });

    }

}