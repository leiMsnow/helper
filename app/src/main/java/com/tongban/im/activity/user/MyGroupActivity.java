package com.tongban.im.activity.user;


import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.MyGroupAdapter;
import com.tongban.im.model.Group;

import java.util.ArrayList;
import java.util.List;

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
        List<Group> myGroupList = new ArrayList<>();
        Group group = new Group();
        group.setGroup_avatar("http://g.hiphotos.baidu.com/image/w%3D310/sign=849647306963f6241c5d3f02b745eb32/5882b2b7d0a20cf4472432d674094b36acaf9907.jpg");
        group.setGroup_name("2014年7月宝宝圈");
        myGroupList.add(group);
        mAdapter = new MyGroupAdapter(mContext, R.layout.item_group_list, myGroupList);
        lvMyGroupList.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {

    }
}
