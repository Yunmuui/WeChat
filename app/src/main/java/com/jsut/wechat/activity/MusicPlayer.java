package com.jsut.wechat.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.jsut.wechat.R;
import com.jsut.wechat.service.MusicService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button selectAndPlayMusicButton, pauseOrContinueMusicButton, finishMusicButton, replayMusicButton,startOrFinishMusicService;
    private boolean isPause = false;
    private List<String> musicList = new ArrayList<>();
    private int selectedMusicIndex = -1;

    private boolean isMusicServiceRunning = false;

    private SeekBar mSeekBar;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //设置状态栏
        setStatusBar();

        // 初始化UI控件
        selectAndPlayMusicButton = findViewById(R.id.selectAndPlayMusicButton);
        pauseOrContinueMusicButton = findViewById(R.id.pauseOrContinueMusicButton);
        finishMusicButton = findViewById(R.id.finishMusicButton);
        replayMusicButton = findViewById(R.id.replayMusicButton);
        startOrFinishMusicService = findViewById(R.id.startOrFinishMusicService);

        // 找到 SeekBar 控件
        mSeekBar = findViewById(R.id.seekBar);

        // 初始化音乐列表
        initMusicList();

        // 判断打开时音乐服务是否存在
        isMusicServiceRunning = isMusicServiceRunning();
        if (isMusicServiceRunning) startOrFinishMusicService.setText("结束后台播放");
        if (isMusicServiceRunning) selectAndPlayMusicButton.setEnabled(false);
        if (isMusicServiceRunning) replayMusicButton.setEnabled(false);
        if (isMusicServiceRunning) mSeekBar.setBackgroundColor(getResources().getColor(R.color.transparent));

        // 设置 SeekBar 监听器
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 根据 SeekBar 的位置修改播放进度
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int duration = mediaPlayer.getDuration();
                    int seekPosition = progress * duration / seekBar.getMax();
                    mediaPlayer.seekTo(seekPosition);
                }
            }
        });
    }

    // 初始化音乐列表, 得到 raw 下的所有音频文件名，并添加到 musicList 中
    private void initMusicList() {
        Field[] fields = R.raw.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.endsWith("_mp3") || name.endsWith("_flac")) {
                    musicList.add(name);
                }
            } catch (Exception e) {
            }
        }
    }

    // 点击“selectAndPlayMusicButton”按钮时，弹出音乐列表对话框
    public void selectAndPlayMusic(View view) {
        final String[] items = new String[musicList.size()];
        musicList.toArray(items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择需要播放的音乐");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedMusicIndex = which;
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = MediaPlayer.create(MusicPlayer.this, getResources().getIdentifier(items[selectedMusicIndex], "raw", getPackageName()));
                // 设置音乐播放完成监听器
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // 音乐播放完成后，释放 MediaPlayer 对象
                        mediaPlayer.release();
                        mediaPlayer = null;
                        // 恢复 SeekBar 的位置
                        mSeekBar.setProgress(0);
                    }
                });
                mediaPlayer.start();
                Toast.makeText(MusicPlayer.this, "正在播放音乐：" + items[selectedMusicIndex], Toast.LENGTH_LONG).show();
                pauseOrContinueMusicButton.setEnabled(true);
                pauseOrContinueMusicButton.setText("॥");
                finishMusicButton.setEnabled(true);
                startOrFinishMusicService.setEnabled(false);

                // 使用 mHandler 定时更新 SeekBar 的进度
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            int duration = mediaPlayer.getDuration();
                            mSeekBar.setMax(duration);
                            mSeekBar.setProgress(currentPosition);
                            mHandler.postDelayed(this, 1000);
                        }
                    }
                }, 1000);
            }
        });
        builder.show();
    }

    // 点击“pauseOrContinueMusicButton”按钮时，播放或暂停音乐
    public void pauseOrContinueMusic(View view) {
        if (mediaPlayer != null) {
            if (isPause) {
                // 如果当前音乐被暂停, 恢复播放
                mediaPlayer.start();
                isPause = false;
                pauseOrContinueMusicButton.setText("॥");
            } else {
                // 暂停当前正在播放的音乐
                mediaPlayer.pause();
                isPause = true;
                pauseOrContinueMusicButton.setText("▶");
            }
        } else {
            Toast.makeText(this, "请先选择需要播放的音乐", Toast.LENGTH_LONG).show();
        }
    }

    public void replayMusic(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            isPause = false;
            pauseOrContinueMusicButton.setText("॥");
        } else {
            Toast.makeText(this, "请先选择需要播放的音乐", Toast.LENGTH_LONG).show();
        }
    }

    public void finishMusic(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPause = false;
            pauseOrContinueMusicButton.setEnabled(false);
            pauseOrContinueMusicButton.setText("▶");
            finishMusicButton.setEnabled(false);
            startOrFinishMusicService.setEnabled(true);
            // 恢复 SeekBar 的位置
            mSeekBar.setProgress(0);
            // 取消更新 SeekBar 的进度
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void startOrFinishMusicService(View view) {
        if (!isMusicServiceRunning) {
            Intent intent = new Intent(this, MusicService.class);
            intent.setAction("START");
            startService(intent);
            isMusicServiceRunning = true;
            startOrFinishMusicService.setText("结束后台播放");
            selectAndPlayMusicButton.setEnabled(false);
            mSeekBar.setBackgroundColor(getResources().getColor(R.color.transparent));
            replayMusicButton.setEnabled(false);
        } else {
            Intent intent = new Intent(this, MusicService.class);
            intent.setAction("STOP");
            stopService(intent);
            isMusicServiceRunning = false;
            startOrFinishMusicService.setText("开启后台播放");
            selectAndPlayMusicButton.setEnabled(true);
            mSeekBar.setBackgroundColor(getResources().getColor(R.color.half_white));
            replayMusicButton.setEnabled(true);
        }
    }


    private boolean isMusicServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices != null) {
            for (ActivityManager.RunningServiceInfo service : runningServices) {
                if (service.service.getClassName().equals("com.zhangyouan.exp3.service.MusicService")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setStatusBar(){
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            ((Window) window).addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.half_transparent));

        }
        //设置状态栏图标颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
