package com.tongban.im.activity.user;


import android.os.Bundle;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.GroupListenerImpl;
import com.tongban.im.model.BaseEvent;

/**
 * 个人中心（我的圈子）
 *
 * @author fushudi
 */
public class MyGroupActivity extends BaseToolBarActivity implements
        OnLoadMoreListener {
    private LoadMoreListView lvMyGroupList;
    private GroupListAdapter mAdapter;

    private int mCursor = 0;
    private int mPageSize = 10;
    private String mUserId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_group;
    }

    @Override
    protected void initView() {
        setTitle(R.string.my_group);
        lvMyGroupList = (LoadMoreListView) findViewById(R.id.lv_my_group_list);
    }

    @Override
    protected void initData() {
        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        lvMyGroupList.setAdapter(mAdapter);
        lvMyGroupList.setPageSize(mPageSize);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mUserId = bundle.getString(Consts.USER_ID);
        } else {
            mUserId = SPUtils.get(mContext, Consts.USER_ID, "").toString();
        }
        UserCenterApi.getInstance().fetchMyGroupsList(mCursor, mPageSize, mUserId, this);
    }

    @Override
    protected void initListener() {
        lvMyGroupList.setOnLoadMoreListener(this);
        mAdapter.setOnClickListener(new GroupListenerImpl(mContext));
    }

    /**
     * 获取我加入的圈子列表Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.MyGroupListEvent obj) {
        mCursor++;
        mAdapter.addAll(obj.myGroupList);
        lvMyGroupList.setResultSize(obj.myGroupList.size());
    }


    @Override
    public void onLoadMore() {
        UserCenterApi.getInstance().fetchMyGroupsList(mCursor, mPageSize, mUserId, this);
    }
}
