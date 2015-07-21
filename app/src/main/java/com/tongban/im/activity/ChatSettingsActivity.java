package com.tongban.im.activity;

import android.os.Bundle;
import android.widget.GridView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.MemberGridAdapter;
import com.tongban.im.model.Group;
import com.tongban.im.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天设置
 */
public class ChatSettingsActivity extends BaseToolBarActivity {

    private GridView gvMembers;
    private MemberGridAdapter mMemberGridAdapter;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat_settings;
    }

    @Override
    protected void initView() {
        gvMembers = (GridView) findViewById(R.id.gv_members);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        List<User> users = new ArrayList<>();
        int i = 0;
        while (true) {
            if (i > 10)
                break;

            User user = new User();
            user.setNick_name("张三" + i);
            user.setPortrait_url("http://www.qjis.com/uploads/allimg/120918/11305V125-21.jpg");
            if (i % 2 == 0) {
                user.setPortrait_url("http://diy.qqjay.com/u2/2012/0601/caafdf4fe2eee3b7f397922b575f46af.jpg");
            }
            users.add(user);
            i++;
        }
        mMemberGridAdapter = new MemberGridAdapter(mContext, R.layout.item_member_grid, users);
        gvMembers.setAdapter(mMemberGridAdapter);
    }

}
