package com.jsut.wechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.R;

import java.util.List;


public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<OneMsg> mMsgList;
    private String user;


    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
        }

    }

    public MsgAdapter(List<com.jsut.wechat.Entity.OneMsg> mMsgList,String user) {
        this.mMsgList = mMsgList;
        this.user=user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wechat_msg_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OneMsg msg = mMsgList.get(position);
        if (msg.getReceiver().equals(user)){
            //如果是收到消息，则显示在左边，将右边布局隐藏
            ((MsgAdapter.ViewHolder) holder).leftLayout.setVisibility(View.VISIBLE);
            ((MsgAdapter.ViewHolder) holder).rightLayout.setVisibility(View.GONE);
            ((ViewHolder) holder).leftMsg.setText(msg.getChatContent());

        }else if(msg.getSender().equals(user)){
            ((MsgAdapter.ViewHolder) holder).rightLayout.setVisibility(View.VISIBLE);
            ((MsgAdapter.ViewHolder) holder).leftLayout.setVisibility(View.GONE);
            ((MsgAdapter.ViewHolder) holder).rightMsg.setText(msg.getChatContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
