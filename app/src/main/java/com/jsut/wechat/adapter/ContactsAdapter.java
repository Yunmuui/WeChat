package com.jsut.wechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsut.wechat.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<String> mData;
    private OnDeleteClickListener mOnDeleteClickListener;

    //item的viewholder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView friend;

        public Button delete_friend;

        public ViewHolder(View v) {
            super(v);
            friend = v.findViewById(R.id.friend);
            delete_friend=v.findViewById(R.id.delete_friend);
        }
    }

    public ContactsAdapter(ArrayList<String> data) {
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_item, parent, false);
        return new com.jsut.wechat.adapter.ContactsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).friend.setText(mData.get(position));
        // 为删除按钮设置点击事件
        ((ViewHolder) holder).delete_friend.setOnClickListener(v -> {
            if (mOnDeleteClickListener != null) {
                mOnDeleteClickListener.onDeleteClick(mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 自定义添加数据方法
    public void addData(List<String> newData) {
        mData.addAll(newData);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String friend);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        mOnDeleteClickListener = listener;
    }
}
