package com.tongban.im.activity.user;


import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心（我的圈子）
 *
 * @author fushudi
 */
public class MyGroupActivity extends BaseToolBarActivity {
    private ListView lvMyGroupList;
    private GroupListAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_group;
    }

    @Override
    protected void initView() {
        setTitle(R.string.my_group);
        lvMyGroupList = (ListView) findViewById(R.id.lv_my_group_list);
    }

    @Override
    protected void initData() {
        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        lvMyGroupList.setAdapter(mAdapter);

        UserCenterApi.getInstance().fetchMyGroupList(0, 10, this);
    }

    @Override
    protected void initListener() {

    }

    public void onEventMainThread(BaseEvent.MyGroupListEvent obj) {

        mAdapter.replaceAll(obj.getMyGroupList());
    }
}
