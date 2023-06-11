package com.jsut.wechat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.jsut.wechat.Dao.ContactsDao;
import com.jsut.wechat.DataBase.ContactsDataBase;
import com.jsut.wechat.Entity.Contact;
import com.jsut.wechat.Entity.User;
import com.jsut.wechat.R;
import com.jsut.wechat.adapter.ContactsAdapter;
import com.jsut.wechat.viewModel.LoginViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements ContactsAdapter.OnDeleteClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private SharedPreferences mSharedPreferences;
    private LoginViewModel mLoginViewModel;
    private Toolbar myToolBar;
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private User user;
    private Context mContext;
    private String mParam1;
    private String mParam2;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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


    public static void view(View v){
        TextView tv;
        tv=(TextView)v.findViewById(R.id.friend);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        // 获取toolbar
        myToolBar = view.findViewById(R.id.mytoolbar);
        setToolBarListener(myToolBar);
        // 获取 ViewModel 实例
        mLoginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        //获得recyclerView
        recyclerView = view.findViewById(R.id.contacts_recyclerView);
        // 刷新联系人列表
        refreshFriendList(recyclerView);
        return view;
    }

    public void refreshFriendList(){
        refreshFriendList(recyclerView);
    }

    private void refreshFriendList(RecyclerView recyclerView) {
        // 查询数据库、创建适配器、刷新recyclerView
        if(getContext()!=null){
            ContactsDao dao = ContactsDataBase.getDatabaseInstance(getContext()).getContactsDao();
            List<String> friendList = dao.getFriendList(mLoginViewModel.getLoginStatus().getValue());
            contactsAdapter = new ContactsAdapter(new ArrayList<>(friendList),getContext());
            contactsAdapter.setOnDeleteClickListener(this);
            recyclerView.setAdapter(contactsAdapter);
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
                                refreshFriendList(recyclerView);
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

    @Override
    public void onDeleteClick(String friend) {
        ContactsDao dao = ContactsDataBase.getDatabaseInstance(getContext()).getContactsDao();
        String user = mLoginViewModel.getLoginStatus().getValue();
        List<Contact> allDelete = dao.getALLDelete(user,friend);
        dao.deleteAll(allDelete);
        refreshFriendList();
    }
}