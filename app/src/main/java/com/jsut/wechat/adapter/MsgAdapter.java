package com.jsut.wechat.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.R;
import com.jsut.wechat.activity.ChatActivity;

import java.util.List;


public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<OneMsg> mMsgList;
    private String user;


    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg,leftname;
        TextView rightMsg,rightname;
        ImageView leftImage,rightImage;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            rightMsg = view.findViewById(R.id.right_msg);
            leftname=view.findViewById(R.id.left_name);
            rightname=view.findViewById(R.id.right_name);
            leftImage=view.findViewById(R.id.left_image);
            rightImage=view.findViewById(R.id.right_image);
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
            if(msg.getChatType().equals("TEXT")) {
                ((ViewHolder) holder).leftMsg.setText(msg.getChatContent());
                ((ViewHolder) holder).leftImage.setVisibility(View.GONE);
            }else if(msg.getChatType().equals("IMAGE")){
                Bitmap bitmap= ChatActivity.stringtoBitmap(msg.getChatContent());
                ((ViewHolder) holder).leftImage.setImageBitmap(bitmap);
                ((ViewHolder) holder).leftMsg.setVisibility(View.GONE);
            }
            else {
                ((ViewHolder) holder).leftImage.setVisibility(View.GONE);
                ((ViewHolder) holder).leftMsg.setText("语音消息");
            }
            ((ViewHolder) holder).leftname.setText(msg.getSender());
        }else if(msg.getSender().equals(user)){
            ((MsgAdapter.ViewHolder) holder).rightLayout.setVisibility(View.VISIBLE);
            ((MsgAdapter.ViewHolder) holder).leftLayout.setVisibility(View.GONE);
            if(msg.getChatType().equals("TEXT")) {
                ((ViewHolder) holder).rightMsg.setText(msg.getChatContent());
                ((ViewHolder) holder).rightImage.setVisibility(View.GONE);
            }else if(msg.getChatType().equals("IMAGE")){
                Bitmap bitmap= ChatActivity.stringtoBitmap(msg.getChatContent());
                ((ViewHolder) holder).rightImage.setImageBitmap(bitmap);
                ((ViewHolder) holder).rightMsg.setVisibility(View.GONE);
            }
            else{
                ((ViewHolder) holder).rightImage.setVisibility(View.GONE);
                ((ViewHolder) holder).rightMsg.setText("语音消息");
            }
            ((ViewHolder) holder).rightname.setText(msg.getSender());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
