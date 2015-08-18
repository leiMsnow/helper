package com.tongban.im.activity.user;


import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.MyGroupAdapter;

/**
 * 个人中心（我的圈子）
 *
 * @author fushudi
 */
public class MyGroupActivity extends BaseToolBarActivity {
    private ListView lvMyGroupList;
    private MyGroupAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_group;
    }

    @Override
    protected void initView() {
        lvMyGroupList = (ListView) findViewById(R.id.lv_my_group_list);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
