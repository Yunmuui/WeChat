package com.jsut.wechat.fragment;

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

import com.jsut.wechat.Dao.ContactsDao;
import com.jsut.wechat.DataBase.ContactsDataBase;
import com.jsut.wechat.Entity.Contact;
import com.jsut.wechat.R;
import com.jsut.wechat.viewModel.LoginViewModel;

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
        setToolBarListener(myToolBar);
        return view;
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