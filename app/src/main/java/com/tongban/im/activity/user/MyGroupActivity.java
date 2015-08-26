package com.tongban.im.activity.user;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 个人中心（我的圈子）
 *
 * @author fushudi
 */
public class MyGroupActivity extends BaseToolBarActivity implements AdapterView.OnItemClickListener {
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
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String userId = bundle.getString(Consts.USER_ID);
            UserCenterApi.getInstance().fetchMyGroupList(0, 10, userId, this);
        } else {
            UserCenterApi.getInstance().fetchMyGroupList(0, 10, SPUtils.get(mContext, Consts.USER_ID, "").toString(), this);
        }
    }

    @Override
    protected void initListener() {
        lvMyGroupList.setOnItemClickListener(this);
    }

    public void onEventMainThread(BaseEvent.MyGroupListEvent obj) {

        mAdapter.replaceAll(obj.myGroupList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RongIM.getInstance().startGroupChat(mContext, mAdapter.getItem(position).getGroup_id(),
                mAdapter.getItem(position).getGroup_name());
    }
}
