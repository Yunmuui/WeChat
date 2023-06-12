package com.jsut.wechat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.R;
import com.jsut.wechat.activity.ChatActivity;
import com.jsut.wechat.fragment.ChatsFragment;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Chat> mData;

    private Context mContext;

    public ChatsAdapter(ArrayList<Chat> mData,Context context) {
        this.mData=mData;
        this.mContext=context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout oneChat;
        public TextView chat_title;
        public TextView chat_abbreviation;
        public TextView chat_time;

        public ViewHolder(View v) {
            super(v);
            oneChat=v.findViewById(R.id.oneChat);
            chat_title = v.findViewById(R.id.chat_title);
            chat_abbreviation = v.findViewById(R.id.chat_abbreviation);
            chat_time = v.findViewById(R.id.chat_time);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wechat_chats_item, parent, false);
        return new com.jsut.wechat.adapter.ChatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ChatsAdapter.ViewHolder) holder).chat_title.setText(mData.get(position).getChatTitle());
        ((ChatsAdapter.ViewHolder) holder).chat_abbreviation.setText(mData.get(position).getChatAbbreviation());
        ((ChatsAdapter.ViewHolder) holder).chat_time.setText(mData.get(position).getChatTime());
        // 为此条聊天设置点击事件
        ((ChatsAdapter.ViewHolder) holder).oneChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mData.get(position).getUser();
                String chatTitle = mData.get(position).getChatTitle(); // 获取聊天标题
                int Id = mData.get(position).getId(); // 获取聊天id

                // 创建Intent对象，并传递参数
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("chatTitle", chatTitle);
                intent.putExtra("id", Id);

                mContext.startActivity(intent); // 启动ChatActivity
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
