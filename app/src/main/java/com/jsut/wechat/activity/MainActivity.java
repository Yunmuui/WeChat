package com.jsut.wechat.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jsut.wechat.Dao.ChatsDao;
import com.jsut.wechat.Dao.RemoteMsgDao;
import com.jsut.wechat.Dao.UserDao;
import com.jsut.wechat.DataBase.ChatsDatabase;
import com.jsut.wechat.DataBase.RemoteMsgDatabase;
import com.jsut.wechat.DataBase.UserDatabase;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.Entity.User;
import com.jsut.wechat.R;
import com.jsut.wechat.fragment.ChatsFragment;
import com.jsut.wechat.fragment.ContactsFragment;
import com.jsut.wechat.fragment.DiscoverFragment;
import com.jsut.wechat.fragment.MeFragment;
import com.jsut.wechat.viewModel.LoginViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager;
    private Fragment chatsFragment,contactsFragment,discoverFragment,meFragment;
    private List<Fragment> fragmentList;
    //public SharedPreferences mSharedPreferences;
    private LoginViewModel mLoginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏
        setStatusBar();


        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //用户登陆显示
        user_name();
        //更新本地数据库
        receiver();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        viewPager=findViewById(R.id.viewpager);

        chatsFragment = new ChatsFragment();
        contactsFragment = new ContactsFragment();
        discoverFragment = new DiscoverFragment();
        meFragment = new MeFragment();

        // 创建Fragment列表
        fragmentList = new ArrayList<>();
        fragmentList.add(chatsFragment);
        fragmentList.add(contactsFragment);
        fragmentList.add(discoverFragment);
        fragmentList.add(meFragment);

        // 创建viewPager adapter，并绑定ViewPager2
        viewPager.setAdapter(new ViewPager2Adapter(fragmentList));

        // 绑定底部导航栏与ViewPager2
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.chats) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.contacts) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.discover) {
                    viewPager.setCurrentItem(2);
                    return true;
                } else if (itemId == R.id.me) {
                    viewPager.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });

        // 监听viewPager滑动事件，更新底部导航栏的选中状态
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.chats);
                        if(chatsFragment!=null) ((ChatsFragment)chatsFragment).refreshChatsList();
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.contacts);
                        if(contactsFragment!=null) ((ContactsFragment)contactsFragment).refreshFriendList();
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.discover);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.me);
                        break;
                }
            }
        });

        //实例化mSharedPreferences
        //mSharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE);
    }


    public void sendNotification(View view) {
        //判断是否有 POST_NOTIFICATIONS 权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            // 已授予 POST_NOTIFICATIONS 权限，可以发布通知
        } else {
            // 没有 POST_NOTIFICATIONS 权限，向用户申请该权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
        }
        //创建NotificationManager对象
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //版本高时，设置channelId
        /*
         *           IMPORTANCE_NONE  关闭通知
         *           IMPORTANCE_MIN   开启通知   不会弹出  没有提示音  状态栏无显示
         *           IMPORTANCE_LOW   开启通知   不会弹出  没有提示音  状态栏中显示
         *           IMPORTANCE_DEFAULT   开启通知   不会弹出  发出提示音  状态栏中显示
         *           IMPORTANCE_HIGH   开启通知   会弹出  发出提示音  状态栏中显示
         * */
        String channelId = String.valueOf(new Random().nextInt());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1) {
            NotificationChannel channel = new NotificationChannel(channelId, "通知", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        //创建一个Intent并为它定义操作
        Intent intent = new Intent(this, WeChatMomentsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                //设置通知的属性(至少有3个必备属性)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Title")
                .setContentText("Content")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        /*                设置通知的属性(至少有3个必备属性)
        *                 .setContentTitle("今日热搜")
        *                 .setContentText("听说xxxx今天翻车了")
        *                 .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
        *                   辅助属性（大的图标和颜色）
        *                 .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.rabbit))
        *               .setColor(Color.parseColor("#656565"))
        *           点击跳转意图的动作（点击通知跳转到对应的内容）
        *            .setContentIntent(pendingIntent)
        *            .setAutoCancel(true)
        *            .setWhen(System.currentTimeMillis())
        *              设置顶部文字显示
        *            .setTicker(text)
        *            设置振动效果
        *        .setVibrate(new long[]{100, 200, 300, 400, 500, 300, 200, 400})
        *        灯光，铃声，导航等
        * */
        notificationManager.notify(1, builder.build());
    }

    private class ViewPager2Adapter extends FragmentStateAdapter {

        private List<Fragment> fragmentList;

        public ViewPager2Adapter(List<Fragment> fragments) {
            super(MainActivity.this);
            this.fragmentList = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }

    public void setStatusBar(){
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            ((Window) window).addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.wechat_grey));

        }
        //设置状态栏图标颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public void login(View view) {
        //获得editor
        //SharedPreferences.Editor editor = mSharedPreferences.edit();
        // 获得按钮中的文字以确定登录用户
        String user = ((Button)view).getText().toString();
        // 登录成功
        Toast.makeText(this,"登录成功",Toast.LENGTH_LONG).show();
        // 将登录状态设为user
        mLoginViewModel.setLoginStatus(user);
    }
    public void user_name(){
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        UserDao userDao = UserDatabase.getDatabaseInstance(MainActivity.this).getUserDao();
        User user=new User();
        user.phone=bundle.getString("phone");
        List<User> user1List = userDao.getallUserByPhone(user.phone);
        for (User user1 : user1List) {
            if(user.phone.equals(user1.phone)){
                mLoginViewModel.setLoginStatus(user1.name);
            }
        }
    }

    public void receiver(){
        String abbrevuation = null;
        UserDao userDao = UserDatabase.getDatabaseInstance(MainActivity.this).getUserDao();
        ChatsDao dao = ChatsDatabase.getDatabaseInstance(MainActivity.this).getChatsDao();
        RemoteMsgDao far_dao= RemoteMsgDatabase.getDatabaseInstance(MainActivity.this).getRemoteMsgDao();
        //查询正在登录用户
        String username=String.valueOf(mLoginViewModel.getLoginStatus().getValue());
        //System.out.print(username);
        //检索远程数据库与登录用户相关信息
        List<OneMsg> far_Msglist=far_dao.getMsgList(username);

        List<Chat> chatList=dao.getChatsListByUser(username);
        if(chatList.size()==0) {
            Chat chat=new Chat(username,"","","",far_Msglist);
            chatList.add(chat);
            dao.insert(chat);
        }
        for(OneMsg msg:far_Msglist) {
            for (Chat one : chatList) {
                if (msg.getSender().equals(one.chatTitle)) {
                    one.addOneMsg(msg);
                    abbrevuation = msg.getChatContent();
                    one.chatAbbreviation=abbrevuation;
                    dao.updateContent(one);
                }
                else{
                    List<OneMsg>mlist=new ArrayList<>();
                    Chat chat=new Chat(username, msg.getSender(),msg.getChatContent(),"0",mlist);
                    chat.addOneMsg(msg);
                    dao.insert(chat);
                }
            }
        }
       for (Chat one : chatList){
           if(one.chatTitle.equals("")){
               dao.delete(one);
           }
       }
        //删除远程数据库内容
        far_dao.deleteAll(far_Msglist);
    }
}
