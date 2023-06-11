package com.jsut.wechat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsut.wechat.Dao.ChatsDao;
import com.jsut.wechat.DataBase.ChatsDatabase;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.Entity.User;
import com.jsut.wechat.R;
import com.jsut.wechat.adapter.MsgAdapter;
import com.jsut.wechat.fragment.ChatsFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 101;
    public static final int CAMERA_PERMISSION_REQUEST_CODE=102;
    TextView chat_name;
    String user;
    RecyclerView msg_recycle;

    MsgAdapter msgAdapter;
    EditText input;

    Button back;
    ImageFilterButton shoot_button;
    ImageView shoot_photo;
    private Uri imageUri;

    LinearLayout more_menu;
    LinearLayout chat_window;

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
        more_menu=findViewById(R.id.more_menu);
        chat_window=findViewById(R.id.chat_window);
        input = (EditText)findViewById(R.id.input_text);
        Button send = (Button) findViewById(R.id.send);
        shoot_button = findViewById(R.id.shoot_button);
        shoot_photo = findViewById(R.id.shoot_photo);

        //设置控件
        chat_name.setText(chatTitle);
        setOnTouchToCloseMoreMenu();
        setOnClickToShoot();

        //信息显示
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msg_recycle.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(chatContent,user);
        msg_recycle.setAdapter(msgAdapter);

        //打开数据库
        ChatsDao dao = ChatsDatabase.getDatabaseInstance(ChatActivity.this).getChatsDao();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent =new Intent(ChatActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });

        send.setOnClickListener(v -> {
            String content = input.getText().toString();

            if(!"".equals(content)){
                OneMsg msg =new OneMsg(user,chatTitle,"text",content,"0");
                //修改数据库信息
                List<Chat> chatsList =dao.getChatsList(user);
                Chat chat = null;
                for(Chat chat1:chatsList){
                    if(chat1.getId()==id){
                         chat=chat1;
                    }
                }
                chatContent.add(msg);
                chat.chatContent=chatContent;
                dao.updateContent(chat);
                //当有新消息，刷新RecyclerView的显示
                msgAdapter.notifyItemInserted(chatContent.size());
                //将RecyclerView定位到最后一行
                msg_recycle.scrollToPosition(chatContent.size());
                //清空输入框内容
                input.setText("");
            }
        });
    }

    /**
     * 获取活动或片段的位图和图像路径onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                shoot_photo.setImageBitmap(bitmap); // 展示刚拍过的照片
                getImgBase64(shoot_photo); // 直接把 imageview 取出图片转换为base64格式
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }

    }


    /**
     * imageview取出图片转换为base64格式
     *
     * @param imageView
     * @return
     */
    public String getImgBase64(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bb = bos.toByteArray();
        String image = Base64.encodeToString(bb, Base64.NO_WRAP);
        return image;
    }
    /**
     * 将字符串转换成Bitmap类型
     *
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string,ImageView imageView) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);

            imageView.setImageBitmap(bitmap); // 展示图片

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void setOnClickToShoot() {
        shoot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //无相机权限
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[] { Manifest.permission.CAMERA }, CAMERA_PERMISSION_REQUEST_CODE);
                }else{
                    //有相机权限
                    startCamera();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 相机权限已经被授予
                startCamera();
            } else {
                // 相机权限被拒绝
                Toast.makeText(this, "相机权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        //创建file对象，用于存储拍照后的图片；
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(ChatActivity.this,
                    "com.jsut.wechat.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void setOnTouchToCloseMoreMenu() {
        //设置窗体的触摸事件来关闭抽屉
        chat_window.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                close_more_menu();
                return false;
            }
        });
        //设置输入框的触摸事件来关闭抽屉，避免点击事件被输入框吃掉
        input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                close_more_menu();
                return false;
            }
        });
        //设置recycleView的触摸事件来关闭抽屉，避免点击事件被recycleView吃掉
        msg_recycle.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                close_more_menu();
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // 这里空着就好
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // 这里空着就好
            }
        });
    }

    public void close_more_menu(){
        //获取more_menu参数
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) more_menu.getLayoutParams();
        //如果更多菜单抽屉打开
        if(params.weight==1){
            //设置更多菜单weight为0
            params.weight=0;
            //更新更多菜单参数，使其关闭
            more_menu.setLayoutParams(params);
        }
    }

    public void open_more_menu(View view) {
        //获取more_menu参数
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) more_menu.getLayoutParams();
        //修改weight，从0到1或从1到0
        params.weight=params.weight==1?0:1;
        //设置参数，通过weight改变实现抽屉效果
        more_menu.setLayoutParams(params);
    }
}