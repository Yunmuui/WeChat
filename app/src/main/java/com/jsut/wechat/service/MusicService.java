package com.jsut.wechat.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jsut.wechat.R;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {

    private static final String TAG = "MusicService";

    private MediaPlayer mMediaPlayer;
    private List<String> musicList = new ArrayList<>();
    private int mCurrentPosition = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
        initMusicList();
    }

    // 初始化音乐列表, 得到 raw 下的所有音频文件名，并添加到 musicList 中
    private void initMusicList() {
        Field[] fields = R.raw.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.endsWith("_mp3")||name.endsWith("_flac")) {
                    musicList.add(name);
                }
            } catch (Exception e) {
            }
        }
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("START")) {
            startPlay();
        } else if (intent.getAction().equals("STOP")) {
            stopPlay();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startPlay() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }

        mCurrentPosition = (mCurrentPosition + 1) % musicList.size();

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + musicList.get(mCurrentPosition));
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "play error", e);
        }
    }

    private void stopPlay() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


}
