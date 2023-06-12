package com.jsut.wechat.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.jsut.wechat.Dao.ChatsDao;
import com.jsut.wechat.Dao.ContactsDao;
import com.jsut.wechat.Dao.UserDao;
import com.jsut.wechat.DataBase.ChatsDatabase;
import com.jsut.wechat.DataBase.ContactsDataBase;
import com.jsut.wechat.DataBase.UserDatabase;
import com.jsut.wechat.Entity.Chat;
import com.jsut.wechat.Entity.Contact;
import com.jsut.wechat.Entity.OneMsg;
import com.jsut.wechat.Entity.User;
import com.jsut.wechat.R;
import com.jsut.wechat.activity.ChatActivity;
import com.jsut.wechat.activity.FriendDetailActivity;
import com.jsut.wechat.activity.LoginActivity;
import com.jsut.wechat.adapter.ChatsAdapter;
import com.jsut.wechat.adapter.ContactsAdapter;
import com.jsut.wechat.viewModel.LoginViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Toolbar myToolBar;
    private TextView mUserNameTextView;

    private LoginViewModel mLoginViewModel;
    private RecyclerView recyclerView;

    private ChatsAdapter chatsAdapter;
    private ContactsDao dao;

    public ChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatsFragment newInstance(String param1, String param2) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        // 找到textview
        mUserNameTextView = view.findViewById(R.id.show_logined_user);

        // 获取 ViewModel 实例
        mLoginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        // 观察 LiveData，并在状态改变时更新 TextView 组件的文本内容
        mLoginViewModel.getLoginStatus().observe(getViewLifecycleOwner(), loginStatus -> mUserNameTextView.setText("登录用户:"+loginStatus));
        // 获取toolbar
        myToolBar = view.findViewById(R.id.mytoolbar);
        // 获得recyclerView
        recyclerView = view.findViewById(R.id.chats_recyclerView);
        // 刷新聊天列表
        refreshChatsList(recyclerView);
        setToolBarListener(myToolBar);
        return view;
    }

    public void refreshChatsList(){
        refreshChatsList(recyclerView);
    }
    private void refreshChatsList(RecyclerView recyclerView) {
        // 查询数据库、创建适配器、刷新recyclerView
        if(getContext()!=null){
            ChatsDao dao = ChatsDatabase.getDatabaseInstance(getContext()).getChatsDao();
            //dao.deleteAll();
            OneMsg oneChat1 = new OneMsg("用户A", "用户B", "TEXT", "你好", "2小时前");
            OneMsg oneChat2 = new OneMsg("用户B", "用户A", "TEXT", "你在哪", "1小时前");
            List<OneMsg> test = new ArrayList<>();
            test.add(oneChat1);
            test.add(oneChat2);
            dao.insert(new Chat("用户A", "用户B", "你在哪", "1小时前",test));
            List<Chat> chatsList = dao.getChatsListByUser(mLoginViewModel.getLoginStatus().getValue());

            chatsAdapter = new ChatsAdapter(new ArrayList<>(chatsList),getContext());
            //chatsAdapter.setOnDeleteClickListener(this);
            recyclerView.setAdapter(chatsAdapter);
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

    public void setToolBarListener(Toolbar toolbar) {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.search) {// 处理搜索菜单项的点击事件
                    return true;
                } else if (itemId == R.id.startChats) {// 处理发起群聊菜单项的点击事件
                    // 创建一个新的多选框对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("请选择人员");
                    dao = ContactsDataBase.getDatabaseInstance(getContext()).getContactsDao();
                    List<String> friendList = dao.getFriendList(mLoginViewModel.getLoginStatus().getValue());
                    // 设置多选框的选项
                    String[] items = friendList.toArray(new String[0]);
                    boolean[] checkedItems = {false, false, false, false, false};
                    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            // 处理多选框中的选择事件
                        }
                    });

                    // 设置多选框的确定按钮
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 处理多选框确定按钮的点击事件
                            // 获取多选框的 ListView 控件对象
                            ListView listView = ((AlertDialog) dialog).getListView();

                            // 定义计数器变量，用于记录选中的项的数量
                            int count = 0;
                            // 定义一个列表，用于存储选中的项
                            List<String> selectedItems = new ArrayList<>();
                            // 遍历 ListView 中的所有项，判断哪些项被选中
                            for (int i = 0; i < listView.getCount(); i++) {
                                if (listView.isItemChecked(i)) {
                                    count++;
                                    // 如果这个项被选中，将它添加到列表中
                                    selectedItems.add(items[i]);
                                }
                            }
                            selectedItems.add(mLoginViewModel.getLoginStatus().getValue());
                            if(count>0){
                                ChatsDao dao = ChatsDatabase.getDatabaseInstance(getContext()).getChatsDao();
                                List<Chat> chatList = dao.getChatsListByUser(mLoginViewModel.getLoginStatus().getValue());
                                Boolean flag = false;
                                for(Chat chat:chatList){
                                    Collections.sort(selectedItems);
                                    if(chat.getChatTitle().equals(String.join("、", selectedItems))){
                                        Intent intent = new Intent();
                                        intent.putExtra("user",mLoginViewModel.getLoginStatus().getValue());
                                        intent.putExtra("chatTitle", chat.getChatTitle());
                                        intent.putExtra("id", chat.getId());
                                        intent.setClass(getContext(), ChatActivity.class);
                                        startActivity(intent);
                                        flag = true;
                                    }
                                }
                                if (!flag){
                                    Chat chat = new Chat(mLoginViewModel.getLoginStatus().getValue(), String.join("、", selectedItems), "", "0", new ArrayList<OneMsg>());
                                    long id = dao.insertAndReturnId(chat);
                                    Intent intent = new Intent();
                                    intent.putExtra("user",mLoginViewModel.getLoginStatus().getValue());
                                    intent.putExtra("chatTitle", chat.getChatTitle());
                                    intent.putExtra("id", id);
                                    intent.setClass(getContext(), ChatActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    });

                    // 显示多选框对话框
                    builder.show();
                    return true;
                } else if (itemId == R.id.addFriends) {// 处理添加朋友菜单项的点击事件
                    // 弹出对话框让用户输入好友的姓名
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("添加朋友");
                    final EditText etFriendName = new EditText(getContext());
                    builder.setView(etFriendName);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(mLoginViewModel.getLoginStatus().getValue()!=null){
                                // 获取用户输入的好友姓名
                                String friendName = etFriendName.getText().toString();
                                // 获取用户名
                                String user = mLoginViewModel.getLoginStatus().getValue();
                                // 插入好友信息到数据库
                                ContactsDao dao = ContactsDataBase.getDatabaseInstance(getContext()).getContactsDao();
                                dao.insert(new Contact(user,friendName));
                                // 刷新好友列表
                                //refreshFriendList(recyclerView);
                            }else{
                                Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                    return true;
                } else if (itemId == R.id.sacn) {// 处理扫一扫菜单项的点击事件
                    return true;
                } else if (itemId == R.id.pay) {// 处理收付款菜单项的点击事件
                    return true;
                }
                return false;
            }
        });
    }

}