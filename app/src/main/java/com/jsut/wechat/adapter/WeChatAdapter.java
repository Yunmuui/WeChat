package com.jsut.wechat.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jsut.wechat.Bean.FriendBean;
import com.jsut.wechat.R;

import java.util.ArrayList;
import java.util.List;

public class WeChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int VIEW_TYPE_COVER = 0;
    public final int VIEW_TYPE_ITEM = 1;
    private ArrayList<FriendBean> mData;
    public int inputCover;
    public String inputUser;
    //封面的viewholder
    public static class CoverViewHolder extends RecyclerView.ViewHolder {
        public ImageView cover;
        public TextView user;
        public ImageFilterButton profile_picture;

        public CoverViewHolder(View v) {
            super(v);
            cover = v.findViewById(R.id.moments_cover);
            user = v.findViewById(R.id.moments_user);
            profile_picture = v.findViewById(R.id.moments_profile_picture);
        }
    }
    //item的viewholder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView friend_profile_picture;
        public TextView friend_name;
        public TextView friend_text;
        public TextView friend_publish_time;

        public ItemViewHolder(View v) {
            super(v);
            friend_profile_picture = v.findViewById(R.id.friend_profile_picture);
            friend_name = v.findViewById(R.id.friend_name);
            friend_text = v.findViewById(R.id.friend_text);
            friend_publish_time = v.findViewById(R.id.friend_publish_time);
        }
    }

    public WeChatAdapter(ArrayList<FriendBean> data,int cover,String user) {
        mData = data;
        inputCover = cover;
        inputUser = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_COVER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wechat_moments_cover, parent, false);
            return new CoverViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wechat_moments_item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CoverViewHolder) {
            // 处理封面相关逻辑
            ((CoverViewHolder) holder).cover.setImageResource(inputCover);
            ((CoverViewHolder) holder).user.setText(inputUser);
        } else if (holder instanceof ItemViewHolder) {
            // 处理普通Item相关逻辑
            String friendProfilePicture = mData.get(position - 1).getFriend_profile_picture();
            int resId = holder.itemView.getContext().getResources()
                    .getIdentifier(friendProfilePicture, "drawable", holder.itemView.getContext().getPackageName());
            Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), resId);
            ((ItemViewHolder) holder).friend_profile_picture.setBackground(drawable);
            ((ItemViewHolder) holder).friend_name.setText(mData.get(position - 1).getFriend_name());
            ((ItemViewHolder) holder).friend_text.setText(mData.get(position - 1).getFriend_text());
            ((ItemViewHolder) holder).friend_publish_time.setText(mData.get(position - 1).getFriend_publish_time());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    // 自定义添加数据方法
    public void addData(List<FriendBean> newData) {
        mData.addAll(newData);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_COVER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }
}
